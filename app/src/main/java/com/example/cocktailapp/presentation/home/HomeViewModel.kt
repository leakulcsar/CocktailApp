package com.example.cocktailapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailapp.R
import com.example.cocktailapp.domain.Cocktail
import com.example.cocktailapp.domain.CocktailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cocktailRepo: CocktailRepository,
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState.initial())
    val homeUiState = _homeUiState.asStateFlow()

    private val _searchQuery = Channel<String>()

    init {
        observeSearchQuery()
    }

    fun handleIntent(cocktailIntent: CocktailIntent) = viewModelScope.launch {
        when (cocktailIntent) {
            is CocktailIntent.LoadCocktailOfTheDay -> loadCocktailOfTheDay()
            CocktailIntent.LoadFavoriteCocktails -> loadFavorites()
            CocktailIntent.ClearError -> clearError()
            CocktailIntent.ClearSearch -> clearSearch()
            is CocktailIntent.CocktailSelected -> onCocktailSelected(cocktailIntent.cocktail)
            CocktailIntent.ManualSearch -> onSearch()
            is CocktailIntent.SearchQueryChanged -> onSearchQueryChanged(cocktailIntent.query)
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        _searchQuery.receiveAsFlow()
            .debounce(300L)
            .onEach { query ->
                if (query.isEmpty()) {
                    _homeUiState.update {
                        it.copy(isSearching = false, searchResult = emptyList())
                    }
                } else {
                    _homeUiState.update {
                        it.copy(isSearching = true)
                    }
                    cocktailRepo.getCocktails(query).onSuccess { searchResult ->
                        _homeUiState.update {
                            it.copy(isSearching = false, searchResult = searchResult)
                        }

                    }.onFailure {
                        _homeUiState.update {
                            it.copy(
                                isSearching = false,
                                errorMessage = R.string.home_failure_search
                            )
                        }
                    }
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private suspend fun loadCocktailOfTheDay() {
        cocktailRepo.getRandomCocktail().onSuccess { randomCocktail ->
            _homeUiState.update { currentState ->
                currentState.copy(randomCocktail = randomCocktail)
            }

        }.onFailure {
            _homeUiState.update { currentState ->
                currentState.copy(errorMessage = R.string.home_failure_cocktail_of_the_day)
            }
        }
    }

    private suspend fun onSearchQueryChanged(text: String) {
        _homeUiState.update { currentState ->
            currentState.copy(query = text)
        }
        _searchQuery.send(text)
    }

    private suspend fun onSearch() {
        _searchQuery.send(_homeUiState.value.query)
    }

    private suspend fun onCocktailSelected(cocktail: Cocktail) {
        cocktailRepo.isCocktailAvailable(cocktail.id)
            .onSuccess { alreadyExists -> if (!alreadyExists) cocktailRepo.saveCocktail(cocktail) }
            .onFailure { cocktailRepo.saveCocktail(cocktail) }
    }

    private suspend fun loadFavorites() {
        cocktailRepo.getFavorites()
            .collect { favorites ->
                val orderedList = favorites.sortedBy { it.name }
                _homeUiState.update { currentState ->
                    currentState.copy(favorites = orderedList)
                }
            }
    }

    private fun clearError() {
        _homeUiState.update {
            it.copy(errorMessage = (null))
        }
    }

    private suspend fun clearSearch() {
        onSearchQueryChanged("")
    }
}