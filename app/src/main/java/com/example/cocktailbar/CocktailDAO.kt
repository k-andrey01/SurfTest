package com.example.cocktailbar

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CocktailDao {
    @Insert
    suspend fun insertCocktail(cocktail: CocktailEntity)
}
