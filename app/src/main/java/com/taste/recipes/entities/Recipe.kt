package com.taste.recipes.entities

data class Recipe (
    val id: Long,
    var title: String,
    var ingredients: String,
    var instructions: String,
) {

    companion object {
        const val TABLE_NAME = "Recipes"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_INGREDIENTS = "ingredients"
        const val COLUMN_INSTRUCTIONS = "instructions"
    }
}