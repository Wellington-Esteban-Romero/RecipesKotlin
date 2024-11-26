package com.taste.recipes.data

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("recipes") val recipes: List<RecipeItemResponse>,
    @SerializedName("total") val total: Int,
) {
}

data class RecipeItemResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("ingredients") val ingredients: List<String>,
    @SerializedName("prepTimeMinutes") val prepTimeMinutes: Int,
    @SerializedName("cookTimeMinutes") val cookTimeMinutes: Int,
    @SerializedName("servings") val servings: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("cuisine") val cuisine: String,
    @SerializedName("caloriesPerServing") val caloriesPerServing: Int,
    @SerializedName("image") val image: String,
){}