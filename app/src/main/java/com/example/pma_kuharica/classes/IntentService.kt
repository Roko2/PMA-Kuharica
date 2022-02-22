package com.example.pma_kuharica.classes

open class IntentService(i: Int, s: Food) {
    var mResult = i
    var mResultValue: Food? = s

    fun getResult(): Int {
        return mResult
    }

    fun getResultValue(): Food? {
        return mResultValue
    }
}