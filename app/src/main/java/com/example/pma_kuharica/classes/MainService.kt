package com.example.pma_kuharica.classes

class MainService(i: Int, s: Boolean) {
    var mResult = i
    var mResultValue: Boolean? = s

    fun getResult(): Int {
        return mResult
    }

    fun getResultValue(): Boolean? {
        return mResultValue
    }
}