package com.taste.recipes.data.providers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.taste.recipes.data.entities.Recipe
import com.taste.recipes.utils.DatabaseManager

class RecipeDAO (val context: Context) {

    private lateinit var db: SQLiteDatabase

    private fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    private fun close() {
        db.close()
    }

    private fun getContentValues(recipe: Recipe): ContentValues {
        return ContentValues().apply {
            put(Recipe.COLUMN_NAME_TITLE, recipe.title)
            put(Recipe.COLUMN_INGREDIENTS, recipe.ingredients)
            put(Recipe.COLUMN_INSTRUCTIONS, recipe.instructions)
        }
    }

    fun insert(recipe: Recipe) {
        open()

        // Create a new map of values, where column names are the keys
        val values = getContentValues(recipe)

        try {
            // Insert the new row, returning the primary key value of the new row
            val id = db.insert(Recipe.TABLE_NAME, null, values)
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
    }
}