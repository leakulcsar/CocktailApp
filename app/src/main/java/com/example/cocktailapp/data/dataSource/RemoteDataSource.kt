package com.example.cocktailapp.data.dataSource

import com.example.cocktailapp.data.CocktailsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteDataSource {

    @GET("api/json/v1/1/random.php")
    suspend fun getRandomCocktail() : CocktailsResponseDto

    @GET("api/json/v1/1/search.php")
    suspend fun getCocktails(@Query("s") cocktailQuery: String) : CocktailsResponseDto
}