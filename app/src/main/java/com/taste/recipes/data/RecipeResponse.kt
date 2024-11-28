package com.taste.recipes.data

import com.google.gson.annotations.SerializedName
import com.taste.recipes.data.entities.Recipe

data class RecipeResponse(
    @SerializedName("recipes") val recipes: MutableList<RecipeItemResponse>,
    @SerializedName("total") val total: Int,
) {
}

data class RecipeItemResponse(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("ingredients") val ingredients: List<String>,
    @SerializedName("instructions") val instructions: List<String>,
    @SerializedName("prepTimeMinutes") val prepTimeMinutes: Int,
    @SerializedName("cookTimeMinutes") val cookTimeMinutes: Int,
    @SerializedName("servings") val servings: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("cuisine") val cuisine: String,
    @SerializedName("caloriesPerServing") val caloriesPerServing: Int,
    @SerializedName("image") val image: String,
){}