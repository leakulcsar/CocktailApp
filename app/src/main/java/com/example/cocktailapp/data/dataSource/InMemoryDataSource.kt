package com.example.cocktailapp.data.dataSource

import com.example.cocktailapp.domain.Cocktail
import javax.inject.Inject

/**
 * In memory local data source used to store the random cocktail of the day for the time of an app session.
 */
class InMemoryDataSource @Inject constructor() {
    var randomCocktail: Cocktail? = null
}