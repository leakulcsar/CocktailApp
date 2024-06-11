package com.example.cocktailapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cocktailapp.domain.Cocktail
import com.example.cocktailapp.domain.Ingredient
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Data class for the API response.
 */
@JsonClass(generateAdapter = true)
data class CocktailsResponseDto(
    val drinks: List<CocktailDTO>
)

/**
 * Data class for a single cocktail. Used both for the API response and the database entity.
 */
@Entity(tableName = "cocktails")
@JsonClass(generateAdapter = true)
data class CocktailDTO(
    @PrimaryKey
    @ColumnInfo(name = "id") @Json(name = "idDrink") val id: String,
    @ColumnInfo(name = "name") @Json(name = "strDrink") val name: String,
    @ColumnInfo(name = "tags") @Json(name = "strTags") val tags: String?,
    @ColumnInfo(name = "category") @Json(name = "strCategory") val category: String,
    @ColumnInfo(name = "type") @Json(name = "strAlcoholic") val alcoholicType: String,
    @ColumnInfo(name = "glass_type") @Json(name = "strGlass") val glassType: String,
    @ColumnInfo(name = "instructions") @Json(name = "strInstructions") val instructions: String,
    @ColumnInfo(name = "thumbnail_img") @Json(name = "strDrinkThumb") val thumbnailImg: String,
    @ColumnInfo(name = "ingredient1") @Json(name = "strIngredient1") val ingredient1: String?,
    @ColumnInfo(name = "ingredient2") @Json(name = "strIngredient2") val ingredient2: String?,
    @ColumnInfo(name = "ingredient3") @Json(name = "strIngredient3") val ingredient3: String?,
    @ColumnInfo(name = "ingredient4") @Json(name = "strIngredient4") val ingredient4: String?,
    @ColumnInfo(name = "ingredient5") @Json(name = "strIngredient5") val ingredient5: String?,
    @ColumnInfo(name = "ingredient6") @Json(name = "strIngredient6") val ingredient6: String?,
    @ColumnInfo(name = "ingredient7") @Json(name = "strIngredient7") val ingredient7: String?,
    @ColumnInfo(name = "ingredient8") @Json(name = "strIngredient8") val ingredient8: String?,
    @ColumnInfo(name = "ingredient9") @Json(name = "strIngredient9") val ingredient9: String?,
    @ColumnInfo(name = "ingredient10") @Json(name = "strIngredient10") val ingredient10: String?,
    @ColumnInfo(name = "ingredient11") @Json(name = "strIngredient11") val ingredient11: String?,
    @ColumnInfo(name = "ingredient12") @Json(name = "strIngredient12") val ingredient12: String?,
    @ColumnInfo(name = "ingredient13") @Json(name = "strIngredient13") val ingredient13: String?,
    @ColumnInfo(name = "ingredient14") @Json(name = "strIngredient14") val ingredient14: String?,
    @ColumnInfo(name = "ingredient15") @Json(name = "strIngredient15") val ingredient15: String?,
    @ColumnInfo(name = "measure1") @Json(name = "strMeasure1") val measure1: String?,
    @ColumnInfo(name = "measure2") @Json(name = "strMeasure2") val measure2: String?,
    @ColumnInfo(name = "measure3") @Json(name = "strMeasure3") val measure3: String?,
    @ColumnInfo(name = "measure4") @Json(name = "strMeasure4") val measure4: String?,
    @ColumnInfo(name = "measure5") @Json(name = "strMeasure5") val measure5: String?,
    @ColumnInfo(name = "measure6") @Json(name = "strMeasure6") val measure6: String?,
    @ColumnInfo(name = "measure7") @Json(name = "strMeasure7") val measure7: String?,
    @ColumnInfo(name = "measure8") @Json(name = "strMeasure8") val measure8: String?,
    @ColumnInfo(name = "measure9") @Json(name = "strMeasure9") val measure9: String?,
    @ColumnInfo(name = "measure10") @Json(name = "strMeasure10") val measure10: String?,
    @ColumnInfo(name = "measure11") @Json(name = "strMeasure11") val measure11: String?,
    @ColumnInfo(name = "measure12") @Json(name = "strMeasure12") val measure12: String?,
    @ColumnInfo(name = "measure13") @Json(name = "strMeasure13") val measure13: String?,
    @ColumnInfo(name = "measure14") @Json(name = "strMeasure14") val measure14: String?,
    @ColumnInfo(name = "measure15") @Json(name = "strMeasure15") val measure15: String?,
    @ColumnInfo(name = "image_source") @Json(name = "strImageSource") val imageSource: String?,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false,
) {
    fun toCocktail(): Cocktail = Cocktail(
        id = id,
        name = name,
        tags = tags?.split(",") ?: emptyList(),
        category = category,
        type = alcoholicType,
        glassType = glassType,
        instructions = instructions,
        thumbnailImg = thumbnailImg,
        img = imageSource,
        ingredients = buildList {
            ingredient1?.let { add(Ingredient(name = it, measure = measure1.orEmpty())) }
            ingredient2?.let { add(Ingredient(name = it, measure = measure2.orEmpty())) }
            ingredient3?.let { add(Ingredient(name = it, measure = measure3.orEmpty())) }
            ingredient4?.let { add(Ingredient(name = it, measure = measure4.orEmpty())) }
            ingredient5?.let { add(Ingredient(name = it, measure = measure5.orEmpty())) }
            ingredient6?.let { add(Ingredient(name = it, measure = measure6.orEmpty())) }
            ingredient7?.let { add(Ingredient(name = it, measure = measure7.orEmpty())) }
            ingredient8?.let { add(Ingredient(name = it, measure = measure8.orEmpty())) }
            ingredient9?.let { add(Ingredient(name = it, measure = measure9.orEmpty())) }
            ingredient10?.let { add(Ingredient(name = it, measure = measure10.orEmpty())) }
            ingredient11?.let { add(Ingredient(name = it, measure = measure11.orEmpty())) }
            ingredient12?.let { add(Ingredient(name = it, measure = measure12.orEmpty())) }
            ingredient13?.let { add(Ingredient(name = it, measure = measure13.orEmpty())) }
            ingredient14?.let { add(Ingredient(name = it, measure = measure14.orEmpty())) }
            ingredient15?.let { add(Ingredient(name = it, measure = measure15.orEmpty())) }
        },
        isFavorite = isFavorite
    )

    companion object {
        fun from(cocktail: Cocktail): CocktailDTO = CocktailDTO(
            id = cocktail.id,
            name = cocktail.name,
            tags = cocktail.tags.joinToString(","),
            category = cocktail.category,
            alcoholicType = cocktail.type,
            glassType = cocktail.glassType,
            instructions = cocktail.instructions,
            imageSource = cocktail.img,
            thumbnailImg = cocktail.thumbnailImg,
            isFavorite = cocktail.isFavorite,
            ingredient1 = cocktail.ingredients.getOrNull(0)?.name,
            ingredient2 = cocktail.ingredients.getOrNull(1)?.name,
            ingredient3 = cocktail.ingredients.getOrNull(2)?.name,
            ingredient4 = cocktail.ingredients.getOrNull(3)?.name,
            ingredient5 = cocktail.ingredients.getOrNull(4)?.name,
            ingredient6 = cocktail.ingredients.getOrNull(5)?.name,
            ingredient7 = cocktail.ingredients.getOrNull(6)?.name,
            ingredient8 = cocktail.ingredients.getOrNull(7)?.name,
            ingredient9 = cocktail.ingredients.getOrNull(8)?.name,
            ingredient10 = cocktail.ingredients.getOrNull(9)?.name,
            ingredient11 = cocktail.ingredients.getOrNull(10)?.name,
            ingredient12 = cocktail.ingredients.getOrNull(11)?.name,
            ingredient13 = cocktail.ingredients.getOrNull(12)?.name,
            ingredient14 = cocktail.ingredients.getOrNull(13)?.name,
            ingredient15 = cocktail.ingredients.getOrNull(14)?.name,
            measure1 = cocktail.ingredients.getOrNull(0)?.measure,
            measure2 = cocktail.ingredients.getOrNull(1)?.measure,
            measure3 = cocktail.ingredients.getOrNull(2)?.measure,
            measure4 = cocktail.ingredients.getOrNull(3)?.measure,
            measure5 = cocktail.ingredients.getOrNull(4)?.measure,
            measure6 = cocktail.ingredients.getOrNull(5)?.measure,
            measure7 = cocktail.ingredients.getOrNull(6)?.measure,
            measure8 = cocktail.ingredients.getOrNull(7)?.measure,
            measure9 = cocktail.ingredients.getOrNull(8)?.measure,
            measure10 = cocktail.ingredients.getOrNull(9)?.measure,
            measure11 = cocktail.ingredients.getOrNull(10)?.measure,
            measure12 = cocktail.ingredients.getOrNull(11)?.measure,
            measure13 = cocktail.ingredients.getOrNull(12)?.measure,
            measure14 = cocktail.ingredients.getOrNull(13)?.measure,
            measure15 = cocktail.ingredients.getOrNull(14)?.measure
        )
    }
}