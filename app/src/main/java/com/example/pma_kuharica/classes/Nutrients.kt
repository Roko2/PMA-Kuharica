package com.example.pma_kuharica.classes

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Nutrients(
    val chocdf: Double?=0.00,
    val enerc_KCAL: Double?=0.00,
    val fat: Double?=0.00,
    val fibtg: Double?=0.00,
    val procnt: Double?=0.00
) : Serializable