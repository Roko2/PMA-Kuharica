package com.example.pma_kuharica.classes

data class Dish(
    val imageURL: String,
    val ingredients: List<Ingredient>,
    val name: String,
    val originalURL: String,
    val steps: List<String>,
    val timers: List<Int>
)