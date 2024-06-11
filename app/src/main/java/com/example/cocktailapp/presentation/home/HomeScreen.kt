package com.example.cocktailapp.presentation.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.material3.shimmer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cocktailapp.R
import com.example.cocktailapp.domain.Cocktail
import com.example.cocktailapp.presentation.home.CocktailIntent.ClearError
import com.example.cocktailapp.presentation.home.CocktailIntent.ClearSearch
import com.example.cocktailapp.presentation.home.CocktailIntent.CocktailSelected
import com.example.cocktailapp.presentation.home.CocktailIntent.LoadCocktailOfTheDay
import com.example.cocktailapp.presentation.home.CocktailIntent.ManualSearch
import com.example.cocktailapp.presentation.home.CocktailIntent.SearchQueryChanged
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    onNavigateToCocktailDetail: (cocktailId: String) -> Unit,
) {
    val snackbarCoroutineScope = rememberCoroutineScope()
    val state by homeViewModel.homeUiState.collectAsState()

    LaunchedEffect(homeViewModel) {
        homeViewModel.handleIntent(LoadCocktailOfTheDay)
        homeViewModel.handleIntent(CocktailIntent.LoadFavoriteCocktails)
    }

    val onCocktailSelected = remember {
        { cocktail: Cocktail ->
            homeViewModel.handleIntent(CocktailSelected(cocktail))
            onNavigateToCocktailDetail(cocktail.id)
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            Column {
                CenterAlignedTopAppBar(title = { Text(text = "Cocktail App") })
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    homeUiState = state,
                    searchQuery = state.query,
                    onSearchQueryChanged = { homeViewModel.handleIntent(SearchQueryChanged(it)) },
                    onSearch = { homeViewModel.handleIntent(ManualSearch) },
                    onClear = { homeViewModel.handleIntent(ClearSearch) }
                )
            }
        }) { contentPadding ->

        if (state.query.isEmpty()) {
            InitialDashboard(
                modifier = Modifier.padding(contentPadding),
                randomCocktail = state.randomCocktail,
                onCocktailSelected = onCocktailSelected,
                favoriteCocktails = state.favorites
            )
        } else {
            SearchResults(
                modifier = Modifier.padding(contentPadding),
                isSearchInProgress = state.isSearching,
                searchResults = state.searchResult,
                onCocktailSelected = onCocktailSelected
            )
        }
    }

    state.errorMessage?.let { errorMessage ->
        val message = stringResource(errorMessage)
        val action = stringResource(R.string.home_retry)
        snackbarCoroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = action,
                duration = SnackbarDuration.Long
            )
            when (result) {
                SnackbarResult.Dismissed -> homeViewModel.handleIntent(ClearError)
                SnackbarResult.ActionPerformed -> when (errorMessage) {
                    R.string.home_failure_cocktail_of_the_day -> homeViewModel.handleIntent(
                        LoadCocktailOfTheDay
                    )
                    R.string.home_failure_search -> homeViewModel.handleIntent(
                        ManualSearch
                    )
                }
            }
        }
    }
}

@Composable
fun InitialDashboard(
    modifier: Modifier = Modifier,
    randomCocktail: Cocktail?,
    favoriteCocktails: List<Cocktail>,
    onCocktailSelected: (cocktail: Cocktail) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Header(title = "Cocktail of the day")
        }
        item {
            if (randomCocktail != null) {
                CocktailItem(cocktail = randomCocktail, onCocktailSelected = onCocktailSelected)
            } else {
                CocktailPlaceholder()
            }
        }
        item {
            Header(title = "Favorite cocktails")
        }

        if (favoriteCocktails.isEmpty()) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(48.dp)
                ) {
                    Box(modifier = Modifier.size(72.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.ic_no_drinks_24),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = stringResource(R.string.home_no_favorites),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(
                items = favoriteCocktails,
                key = { it.id },
                itemContent = {
                    CocktailItem(
                        cocktail = it,
                        onCocktailSelected = onCocktailSelected
                    )
                }
            )
        }
    }
}

@Composable
fun SearchResults(
    modifier: Modifier = Modifier,
    isSearchInProgress: Boolean,
    searchResults: List<Cocktail>,
    onCocktailSelected: (cocktail: Cocktail) -> Unit,
) {
    if (isSearchInProgress) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        }
    } else if (searchResults.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nothing to show",
                style = MaterialTheme.typography.bodyMedium
            )
        }

    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = searchResults, key = { it.id }) {
                CocktailItem(cocktail = it, onCocktailSelected = onCocktailSelected)
            }
        }
    }
}

@Suppress("DEPRECATION") // Placeholder Shimmer has been deprecated in Accompanist.
@Composable
fun CocktailItem(
    modifier: Modifier = Modifier,
    cocktail: Cocktail,
    onCocktailSelected: (cocktail: Cocktail) -> Unit,
    isShimmerVisible: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCocktailSelected(cocktail) },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        AsyncImage(
            model = cocktail.thumbnailImg,
            contentDescription = null,
            modifier = Modifier
                .size(88.dp)
                .clip(MaterialTheme.shapes.small)
                .placeholder(
                    visible = isShimmerVisible,
                    highlight = PlaceholderHighlight.shimmer(),
                ),
            contentScale = ContentScale.Crop,
        )

        Column {
            Text(
                modifier = Modifier.placeholder(
                    visible = isShimmerVisible,
                    highlight = PlaceholderHighlight.shimmer(),
                ),
                text = cocktail.name,
                style = MaterialTheme.typography.titleMedium,
            )

            Row(
                modifier = Modifier.placeholder(
                    visible = isShimmerVisible,
                    highlight = PlaceholderHighlight.shimmer(),
                ),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Tag(cocktail.type)
                Tag(cocktail.category)
            }
        }
    }
}

@Composable
fun Header(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun Tag(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            )
            .padding(4.dp)
    )
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = homeUiState.query,
        onValueChange = onSearchQueryChanged,
        trailingIcon = {
            if (searchQuery.isNotEmpty()) IconButton(onClear) {
                Icon(
                    painter = painterResource(R.drawable.ic_rounded_cancel_24),
                    contentDescription = "Clear search"
                )
            }
        },
        placeholder = { Text("Search") },
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSend = { onSearch() }),
        singleLine = true,
    )
}

@Composable
private fun CocktailPlaceholder(modifier: Modifier = Modifier) {
    CocktailItem(
        modifier = modifier,
        cocktail = Cocktail(
            id = UUID.randomUUID().toString(),
            name = "Placeholder cocktail",
            category = "Category",
            type = "Type",
            thumbnailImg = "",
            tags = emptyList(),
            ingredients = emptyList(),
            instructions = "",
            glassType = "",
            img = null,
            isFavorite = false
        ),
        onCocktailSelected = {},
        isShimmerVisible = true
    )
}