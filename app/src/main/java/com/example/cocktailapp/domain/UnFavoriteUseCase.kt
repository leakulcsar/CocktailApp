package com.example.cocktailapp.domain

import javax.inject.Inject

class UnFavoriteUseCase @Inject constructor(
    private val cocktailRepository: CocktailRepository
) {

    suspend operator fun invoke(cocktail: Cocktail) =
        cocktailRepository.saveCocktail(cocktail.copy(isFavorite = false))
}