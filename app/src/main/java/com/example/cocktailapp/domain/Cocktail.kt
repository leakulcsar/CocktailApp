package com.example.cocktailapp.domain

import androidx.compose.runtime.Immutable

/**
 * Domain model for cocktails.
 */
@Immutable
data class Cocktail(
    val id: String,
    val name: String,
    val tags: List<String>,
    val category: String,
    val type: String,
    val glassType: String,
    val instructions: String,
    val ingredients: List<Ingredient>,
    val thumbnailImg: String,
    val img: String?,
    val isFavorite: Boolean
)

/**
 * Domain model for ingredients.
 */
data class Ingredient(val name: String, val measure: String)