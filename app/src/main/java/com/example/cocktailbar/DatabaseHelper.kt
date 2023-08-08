package com.example.cocktailbar

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME "
                + "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAME TEXT, "
                + "$COLUMN_DESCRIPTION TEXT, "
                + "$COLUMN_RECIPE TEXT, "
                + "$COLUMN_INGREDIENTS TEXT)")

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "cocktail_database"
        private const val TABLE_NAME = "cocktails"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_RECIPE = "recipe"
        private const val COLUMN_INGREDIENTS = "ingredients"
    }

    fun insertCocktail(cocktail: CocktailEntity) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, cocktail.name)
        values.put(COLUMN_DESCRIPTION, cocktail.description)
        values.put(COLUMN_RECIPE, cocktail.recipe)
        values.put(COLUMN_INGREDIENTS, cocktail.ingredients.joinToString(","))

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllCocktails(): List<CocktailEntity> {
        val cocktails = mutableListOf<CocktailEntity>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                    val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                    val recipe = cursor.getString(cursor.getColumnIndex(COLUMN_RECIPE))
                    val ingredientsStr = cursor.getString(cursor.getColumnIndex(COLUMN_INGREDIENTS))
                    val ingredients = ingredientsStr.split(",").map { it.trim() }

                    val cocktail = CocktailEntity(id, name, description, recipe, ingredients)
                    cocktails.add(cocktail)
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        return cocktails
    }

}
