package com.example.pma_kuharica.classes

import java.io.Serializable

data class Nutrients(
    val CHOCDF: Double?,
    val ENERC_KCAL: Double?,
    val FAT: Double?,
    val FIBTG: Double?,
    val PROCNT: Double?
) : Serializable