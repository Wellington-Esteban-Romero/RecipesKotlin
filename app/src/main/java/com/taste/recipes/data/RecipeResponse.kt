package com.taste.recipes.data

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("recipes") val recipes: List<RecipeItemResponse>,
) {
}

data class RecipeItemResponse(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String
){}