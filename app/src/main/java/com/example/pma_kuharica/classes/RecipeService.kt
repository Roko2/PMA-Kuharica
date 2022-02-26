package com.example.pma_kuharica.classes

open class RecipeService(i: Int, s: Recipe) {
    var mResult = i
    var mResultValue: Recipe? = s

    fun getResult(): Int {
        return mResult
    }

    fun getResultValue(): Recipe? {
        return mResultValue
    }
}