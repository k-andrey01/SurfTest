package com.example.cocktailbar

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CocktailRepository(context: Context) {
    private val cocktailDao: CocktailDao

    init {
        val database = CocktailDatabase.getDatabase(context)
        cocktailDao = database.cocktailDao()
    }

    suspend fun insertCocktail(
        name: String,
        description: String,
        recipe: String,
        ingredients: List<String>
    ) {
        val cocktail = CocktailEntity(
            name = name,
            description = description,
            recipe = recipe,
            ingredients = ingredients
        )
        withContext(Dispatchers.IO) {
            cocktailDao.insertCocktail(cocktail)
        }
    }
}