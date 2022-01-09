package com.example.pma_kuharica.classes

import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName("_links")
    val _links: Links,
    @SerializedName("hints")
    val hints: List<Hint>,
    @SerializedName("parsed")
    val parsed: List<Any>?,
    @SerializedName("text")
    val text: String
)