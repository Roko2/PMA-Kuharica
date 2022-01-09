package com.example.pma_kuharica.classes

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class Hint (
    @SerializedName("food")
    val food: Food? = null,
    @SerializedName("measures")
    val measures: ArrayList<Measure>? = null
)