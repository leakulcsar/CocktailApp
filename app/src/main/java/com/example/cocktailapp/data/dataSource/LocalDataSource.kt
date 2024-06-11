package com.example.cocktailapp.data.dataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cocktailapp.data.CocktailDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDataSource {

    @Query("SELECT * FROM cocktails WHERE is_favorite=1")
    fun getFavoriteCocktails(): Flow<List<CocktailDTO>>

    @Query("SELECT * FROM cocktails WHERE id= :id ")
    fun getCocktailById(id: String): Flow<CocktailDTO?>

    @Query("SELECT EXISTS(SELECT * FROM cocktails WHERE id = :id)")
    suspend fun isCocktailAvailable(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCocktail(cocktail: CocktailDTO)

}