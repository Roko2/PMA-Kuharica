package com.example.pma_kuharica.classes

import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("next")
    val next: Next?=null
)