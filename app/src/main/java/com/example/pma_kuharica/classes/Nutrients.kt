package com.example.pma_kuharica.classes

import java.io.Serializable

data class Nutrients(
    val CHOCDF: Double?=0.00,
    val ENERC_KCAL: Double?=0.00,
    val FAT: Double?=0.00,
    val FIBTG: Double?=0.00,
    val PROCNT: Double?=0.00
) : Serializable