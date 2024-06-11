package com.example.cocktailapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cocktailapp.data.dataSource.LocalDataSource

@Database(entities = [CocktailDTO::class], version = 1)
abstract class CocktailsDatabase : RoomDatabase() {

    abstract fun localDataSource(): LocalDataSource

    companion object {

        @Volatile
        private var instance: CocktailsDatabase? = null

        fun getInstance(context: Context): CocktailsDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): CocktailsDatabase {
            return Room.databaseBuilder(context, CocktailsDatabase::class.java, "cocktails.db")
                .build()
        }
    }
}