package com.example.pma_kuharica.classes

import com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("category")
    val category: String="",
    @SerializedName("categoryLabel")
    val categoryLabel: String?="",
    @SerializedName("foodContentsLabel")
    val foodContentsLabel: String?="",
    @SerializedName("foodId")
    val foodId: String="",
    @SerializedName("image")
    val image: String?="",
    @SerializedName("label")
    val label: String?="",
    @SerializedName("nutrients")
    var nutrients: Nutrients? = null,
    @Transient var isFavorite: Boolean=false,
    @SerializedName("quantity")
    var quantity:Quantity? = null
)