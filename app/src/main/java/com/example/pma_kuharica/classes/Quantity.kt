package com.example.pma_kuharica.classes

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Quantity(
    @SerializedName("foodName")
    val FoodName: String? = "",
    @SerializedName("servingType")
    val ServingType: String?= "",
    @SerializedName("foodWeight")
    val FoodWeight: String? = "",
    @SerializedName("quantitySize")
    val QuantitySize: Double? = 0.0
): Serializable