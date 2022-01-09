package com.example.pma_kuharica.classes

import com.google.gson.annotations.SerializedName

data class Measure(
    @SerializedName("label")
    val label: String,
    @SerializedName("uri")
    val uri: String,
    @SerializedName("weight")
    val weight: Double
)