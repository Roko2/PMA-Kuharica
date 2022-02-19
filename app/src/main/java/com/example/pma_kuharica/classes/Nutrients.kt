package com.example.pma_kuharica.classes

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Nutrients(
    @SerializedName("CHOCDF")
    val chocdf: Double?=0.00,
    @SerializedName("ENERC_KCAL")
    val enerc_KCAL: Double?=0.00,
    @SerializedName("FAT")
    val fat: Double?=0.00,
    @SerializedName("FIBTG")
    val fibtg: Double?=0.00,
    @SerializedName("PROCNT")
    val procnt: Double?=0.00
) : Serializable