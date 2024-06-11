package com.example.cocktailapp.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cocktailapp.domain.Cocktail
import com.example.cocktailapp.domain.Ingredient
import com.example.cocktailapp.presentation.home.Header
import com.example.cocktailapp.presentation.home.Tag

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel,
    onNavigateBack: () -> Unit
) {
    val state by detailViewModel.detailUiState.collectAsState()

    DetailContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onSaveFavorite = {detailViewModel.handleIntent(DetailIntent.SaveCocktail)}
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    state: DetailUiState,
    onNavigateBack: () -> Unit,
    onSaveFavorite: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Navigate back")
                    }
                },
                modifier = modifier.fillMaxWidth()
            )
        },
        floatingActionButton = {
            if (state is DetailUiState.Loaded) {
                ExtendedFloatingActionButton(
                    text = { Text(text = if (state.cocktail.isFavorite) "Unfavorite" else "Favorite") },
                    icon = {
                        Icon(
                            imageVector = if (state.cocktail.isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                            contentDescription = null
                        )
                    },
                    onClick = onSaveFavorite
                )
            }
        }
    ) { contentPadding ->
        when (state) {
            is DetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(Modifier.size(64.dp))
                }
            }

            DetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Error loading detail")
                }
            }

            is DetailUiState.Loaded -> {
                title = state.cocktail.name

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(contentPadding)
                        .padding(16.dp)
                ) {
                    AsyncImage(
                        model = state.cocktail.thumbnailImg,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Header(title = "Preparation instructions")
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = state.cocktail.instructions,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.size(16.dp))
                    Header(title = "Ingredients")
                    Spacer(Modifier.size(4.dp))
                    for (ingredient in state.cocktail.ingredients) {
                        Row {
                            Text(
                                text = ingredient.name,
                                modifier = Modifier.weight(0.5f),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = ingredient.measure,
                                textAlign = TextAlign.End,
                                modifier = Modifier.weight(0.5f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.size(4.dp))
                    }
                    Spacer(Modifier.size(16.dp))
                    Header(title = "Serving glass type")
                    Spacer(Modifier.size(4.dp))
                    Text(
                        text = state.cocktail.glassType,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.size(16.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Tag(text = state.cocktail.category)
                        Tag(text = state.cocktail.type)
                        state.cocktail.tags.filter { it.isNotEmpty() }
                            .onEach { Tag(text = it) }
                    }

                }

            }
        }
    }
}

@Preview
@Composable
fun PreviewDetail() {
    val ingredient = Ingredient(name = "vodka", "2oz")
    val drink = Cocktail(
        id = "id1",
        name = "Random Cocktail",
        category = "drink",
        type = "alcoholic",
        glassType = "big",
        instructions = "Mix everything together",
        thumbnailImg = "https://www.thecocktaildb.com//images//media//drink//ggx0lv1613942306.jpg",
        isFavorite = true,
        img = "https://www.thecocktaildb.com//images//media//drink//ggx0lv1613942306.jpg",
        ingredients = listOf(ingredient, ingredient),
        tags = listOf("summer", "light", "refreshing")
    )
    DetailContent(state = DetailUiState.Loaded(cocktail = drink), onNavigateBack = {}, onSaveFavorite = {})
}