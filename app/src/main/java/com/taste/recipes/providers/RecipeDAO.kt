package com.taste.recipes.providers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.taste.recipes.utils.DatabaseManager

class RecipeDAO (val context: Context) {

    private lateinit var db: SQLiteDatabase

    private fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    private fun close() {
        db.close()
    }
}