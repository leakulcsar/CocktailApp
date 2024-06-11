package com.example.cocktailapp.domain

import kotlinx.coroutines.flow.Flow

interface CocktailRepository {

    suspend fun getRandomCocktail(): Result<Cocktail>

    suspend fun getCocktails(query: String): Result<List<Cocktail>>

    suspend fun saveCocktail(cocktail: Cocktail): Result<Cocktail>

    suspend fun isCocktailAvailable(id: String): Result<Boolean>

    fun getCocktailById(id: String): Flow<Cocktail>

    fun getFavorites(): Flow<List<Cocktail>>
}