package com.example.cocktailapp.presentation.home

import com.example.cocktailapp.domain.Cocktail

sealed interface CocktailIntent {
    data object LoadCocktailOfTheDay : CocktailIntent
    data object LoadFavoriteCocktails : CocktailIntent
    data class CocktailSelected(val cocktail: Cocktail) : CocktailIntent
    data class SearchQueryChanged(val query: String) : CocktailIntent
    data object ManualSearch : CocktailIntent
    data object ClearSearch : CocktailIntent
    data object ClearError : CocktailIntent
}