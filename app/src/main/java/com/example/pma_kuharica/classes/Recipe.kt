package com.example.pma_kuharica.classes

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("recipeId")
    val recipeId:String?="",
    @SerializedName("name")
    val name: String?="",
    @SerializedName("food")
    val food: ArrayList<Food>? =null,
    @SerializedName("description")
    val description: String?=""
)