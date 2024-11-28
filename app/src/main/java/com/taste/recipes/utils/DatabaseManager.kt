package com.taste.recipes.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.taste.recipes.entities.Recipe

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "RecipesDatabase.db"


        private const val SQL_CREATE_TABLE =
            "CREATE TABLE ${Recipe.TABLE_NAME} (" +
                    "${Recipe.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${Recipe.COLUMN_NAME_TITLE} TEXT," +
                    "${Recipe.COLUMN_INGREDIENTS} TEXT," +
                    "${Recipe.COLUMN_INSTRUCTIONS} TEXT)"

        private const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS ${Recipe.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }

    private fun onDestroy(db: SQLiteDatabase) {
        db.execSQL(SQL_DELETE_TABLE)
    }
}