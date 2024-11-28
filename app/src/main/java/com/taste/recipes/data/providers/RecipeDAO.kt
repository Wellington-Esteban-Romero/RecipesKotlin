package com.taste.recipes.data.providers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
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
            put(Recipe.COLUMN_PREP_TIME_MINUTES, recipe.prepTimeMinutes)
            put(Recipe.COLUMN_COOK_TIME_MINUTES, recipe.cookTimeMinutes)
            put(Recipe.COLUMN_SERVINGS, recipe.servings)
            put(Recipe.COLUMN_DIFFICULTY, recipe.difficulty)
            put(Recipe.COLUMN_IMG, recipe.img)
            put(Recipe.COLUMN_CATEGORY, recipe.category)
        }
    }

    fun cursorToEntity(cursor: Cursor): Recipe {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(Recipe.COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(Recipe.COLUMN_NAME_TITLE))
        val ingredients = cursor.getString(cursor.getColumnIndexOrThrow(Recipe.COLUMN_INGREDIENTS))
        val instructions = cursor.getString(cursor.getColumnIndexOrThrow(Recipe.COLUMN_INSTRUCTIONS))
        val prepTimeMinutes = cursor.getInt(cursor.getColumnIndexOrThrow(Recipe.COLUMN_PREP_TIME_MINUTES))
        val cookTimeMinutes = cursor.getInt(cursor.getColumnIndexOrThrow(Recipe.COLUMN_COOK_TIME_MINUTES))
        val servings = cursor.getString(cursor.getColumnIndexOrThrow(Recipe.COLUMN_SERVINGS))
        val difficulty = cursor.getString(cursor.getColumnIndexOrThrow(Recipe.COLUMN_DIFFICULTY))
        val image = cursor.getString(cursor.getColumnIndexOrThrow(Recipe.COLUMN_IMG))
        val category = cursor.getString(cursor.getColumnIndexOrThrow(Recipe.COLUMN_CATEGORY))

        return Recipe(id, name, ingredients, instructions, prepTimeMinutes, cookTimeMinutes, servings, difficulty, image, category)
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

    fun deleteAll() {
        open()

        try {
            // Insert the new row, returning the primary key value of the new row
            val id = db.delete(Recipe.TABLE_NAME, null, null)
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
    }

    fun findAll() : List<Recipe> {
        open()

        var list: MutableList<Recipe> = mutableListOf()

        try {
            val cursor = db.query(
                Recipe.TABLE_NAME,                    // The table to query
                Recipe.COLUMN_NAMES,                  // The array of columns to return (pass null to get all)
                null,// The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                       // don't group the rows
                null,                         // don't filter by row groups
                null                         // The sort order
            )

            while (cursor.moveToNext()) {
                val task = cursorToEntity(cursor)
                list.add(task)
            }
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
        return list
    }

    fun findAllByCategory(idCategory:String) : List<Recipe> {
        open()

        var list: MutableList<Recipe> = mutableListOf()

        try {
            val cursor = db.query(
                Recipe.TABLE_NAME,                    // The table to query
                Recipe.COLUMN_NAMES,                  // The array of columns to return (pass null to get all)
                "${Recipe.COLUMN_CATEGORY} = ?",                       // The columns for the WHERE clause
                arrayOf(idCategory),                   // The values for the WHERE clause
                null,                       // don't group the rows
                null,                         // don't filter by row groups
                null                         // The sort order
            )

            while (cursor.moveToNext()) {
                val task = cursorToEntity(cursor)
                list.add(task)
            }
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
        return list
    }

    fun findRecipeById(idRecipe:String) : List<Recipe> {
        open()

        var list: MutableList<Recipe> = mutableListOf()

        try {
            val cursor = db.query(
                Recipe.TABLE_NAME,                    // The table to query
                Recipe.COLUMN_NAMES,                  // The array of columns to return (pass null to get all)
                "${Recipe.COLUMN_ID} = ?",                       // The columns for the WHERE clause
                arrayOf(idRecipe),                   // The values for the WHERE clause
                null,                       // don't group the rows
                null,                         // don't filter by row groups
                null                         // The sort order
            )

            while (cursor.moveToNext()) {
                val task = cursorToEntity(cursor)
                list.add(task)
            }
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }
        return list
    }
}