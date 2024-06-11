package com.example.cocktailapp.presentation.home

import androidx.annotation.StringRes
import com.example.cocktailapp.domain.Cocktail

data class HomeUiState(
    val randomCocktail: Cocktail?,
    val favorites: List<Cocktail>,
    val query: String,
    val searchResult: List<Cocktail>,
    val isSearching : Boolean,
    @StringRes val errorMessage: Int?
)
{
    companion object {
        fun initial() = HomeUiState(
            query = "",
            favorites = emptyList(),
            randomCocktail = null,
            isSearching = false,
            searchResult = emptyList(),
            errorMessage = null
        )
    }
}