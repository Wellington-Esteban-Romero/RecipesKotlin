package com.taste.recipes.data.entities

data class Recipe (
    val id: Long,
    var title: String,
    var ingredients: String = "",
    var instructions: String = "",
    var prepTimeMinutes: Int = 0,
    var cookTimeMinutes: Int = 0,
    var servings: String = "",
    var difficulty: String = "",
    var img: String = "",
    var category: String = ""
) {

    companion object {
        const val TABLE_NAME = "Recipes"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_INGREDIENTS = "ingredients"
        const val COLUMN_INSTRUCTIONS = "instructions"
        const val COLUMN_PREP_TIME_MINUTES = "prepTimeMinutes"
        const val COLUMN_COOK_TIME_MINUTES = "cookTimeMinutes"
        const val COLUMN_SERVINGS = "servings"
        const val COLUMN_DIFFICULTY = "difficulty"
        const val COLUMN_IMG = "img"
        const val COLUMN_CATEGORY = "category"
        val COLUMN_NAMES = arrayOf(
            COLUMN_ID,
            COLUMN_NAME_TITLE,
            COLUMN_INGREDIENTS,
            COLUMN_INSTRUCTIONS,
            COLUMN_PREP_TIME_MINUTES,
            COLUMN_COOK_TIME_MINUTES,
            COLUMN_SERVINGS,
            COLUMN_DIFFICULTY,
            COLUMN_IMG,
            COLUMN_CATEGORY
        )
    }
}