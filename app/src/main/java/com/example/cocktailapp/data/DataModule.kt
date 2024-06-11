package com.example.cocktailapp.data

import android.content.Context
import com.example.cocktailapp.data.dataSource.RemoteDataSource
import com.example.cocktailapp.data.dataSource.LocalDataSource
import com.example.cocktailapp.domain.CocktailRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindCocktailRepository(cocktailsRepositoryImpl: CocktailRepositoryImpl): CocktailRepository

    companion object {
        @Singleton
        @Provides
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://www.thecocktaildb.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    )
                    .build())
                .build()
        }

        @Provides
        fun provideCocktailApi(retrofit: Retrofit): RemoteDataSource {
            return retrofit.create()
        }

        @Provides
        fun provideCocktailsDatabase(@ApplicationContext applicationContext: Context): CocktailsDatabase =
            CocktailsDatabase.getInstance(applicationContext)

        @Provides
        fun provideLocalDataSource(cocktailsDatabase: CocktailsDatabase): LocalDataSource =
            cocktailsDatabase.localDataSource()
    }
}