package com.example.cocktailapp.presentation.detail

import com.example.cocktailapp.domain.Cocktail

sealed interface DetailUiState {

    data object Loading: DetailUiState
    data class Loaded(val cocktail: Cocktail): DetailUiState
    data object Error: DetailUiState

}