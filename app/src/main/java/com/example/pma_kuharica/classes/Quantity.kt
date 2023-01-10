package com.example.pma_kuharica.classes

import com.google.gson.annotations.SerializedName


data class Quantity(
    val FoodName: String,
    val ServingType: String,
    val FoodWeight: String,
    val QuantitySize: Number
)