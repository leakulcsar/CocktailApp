package com.example.cocktailapp.data

import com.example.cocktailapp.data.dataSource.InMemoryDataSource
import com.example.cocktailapp.data.dataSource.LocalDataSource
import com.example.cocktailapp.data.dataSource.RemoteDataSource
import com.example.cocktailapp.domain.Cocktail
import com.example.cocktailapp.domain.CocktailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CocktailRepositoryImpl @Inject constructor(
    private val cocktailApi: RemoteDataSource,
    private val inMemoryDataSource: InMemoryDataSource,
    private val localDataSource: LocalDataSource
) : CocktailRepository {

    override suspend fun getRandomCocktail(): Result<Cocktail> = withContext(Dispatchers.IO) {
        val cachedRandomCocktail = inMemoryDataSource.randomCocktail
        if (cachedRandomCocktail == null) {
            runCatching { cocktailApi.getRandomCocktail().drinks.first().toCocktail() }
                .onSuccess { inMemoryDataSource.randomCocktail = it }
        } else {
            Result.success(cachedRandomCocktail)
        }

    }

    override suspend fun getCocktails(query: String): Result<List<Cocktail>> =
        withContext(Dispatchers.IO) {
            runCatching {
                cocktailApi.getCocktails(query).drinks.map { it.toCocktail() }
            }
        }

    override suspend fun saveCocktail(cocktail: Cocktail): Result<Cocktail> =
        withContext(Dispatchers.IO) {
            runCatching {
                localDataSource.saveCocktail(CocktailDTO.from(cocktail))
                cocktail
            }
        }

    override suspend fun isCocktailAvailable(id: String): Result<Boolean> = runCatching {
        localDataSource.isCocktailAvailable(id)
    }

    override fun getCocktailById(id: String): Flow<Cocktail> = localDataSource.getCocktailById(id)
        .mapNotNull {
            it?.toCocktail()
        }


    override fun getFavorites(): Flow<List<Cocktail>> =
        localDataSource.getFavoriteCocktails().map { favoriteCocktails ->
            favoriteCocktails.map { it.toCocktail() }
        }
}