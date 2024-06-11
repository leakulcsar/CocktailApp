package com.example.cocktailapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocktailapp.domain.CocktailRepository
import com.example.cocktailapp.domain.MarkCocktailAsFavoriteUseCase
import com.example.cocktailapp.domain.UnFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val cocktailRepository: CocktailRepository,
    private val markCocktailAsFavorite: MarkCocktailAsFavoriteUseCase,
    private val unFavoriteCocktail: UnFavoriteUseCase
) : ViewModel() {

    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState = _detailUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val id: String? = savedStateHandle.get<String>("cocktailId")
            if (id.isNullOrEmpty()) {
                _detailUiState.update {
                    DetailUiState.Error
                }
            } else {
                getCocktailById(id)
            }
        }
    }

    fun handleIntent(intent: DetailIntent) = viewModelScope.launch {
        when (intent) {
            is DetailIntent.GetCocktailById -> getCocktailById(intent.id)
            is DetailIntent.SaveCocktail -> saveFavorite()
        }
    }


    private suspend fun getCocktailById(id: String) {
        cocktailRepository.getCocktailById(id).collect { cocktail ->
            _detailUiState.update {
                DetailUiState.Loaded(cocktail)
            }
        }
    }

    private fun saveFavorite() {
        viewModelScope.launch {
            check(_detailUiState.value is DetailUiState.Loaded)
            val currentLoadedState = _detailUiState.value as DetailUiState.Loaded

            if (currentLoadedState.cocktail.isFavorite) {
                unFavoriteCocktail(currentLoadedState.cocktail)
            } else {
                markCocktailAsFavorite(currentLoadedState.cocktail)
            }
        }
    }
}