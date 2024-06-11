package com.example.cocktailapp.presentation.detail

interface DetailIntent {
    data class GetCocktailById(val id: String) : DetailIntent
    data object SaveCocktail : DetailIntent
}