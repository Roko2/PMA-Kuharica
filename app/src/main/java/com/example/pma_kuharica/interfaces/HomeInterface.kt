package com.example.pma_kuharica.interfaces

import com.example.pma_kuharica.classes.Ingredient

interface HomeInterface {
    fun setIngredient(ingredient: Ingredient)
    fun getIngredient(): Ingredient?
}