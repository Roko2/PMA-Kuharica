package com.example.pma_kuharica.api

import com.example.pma_kuharica.interfaces.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager private constructor() {
    private val service: ApiService
    fun service(): ApiService {
        return service
    }

    companion object {
        var instance: ApiManager? = null
        fun getNewInstance(): ApiManager? {
            if (instance == null) {
                instance = ApiManager()
            }
            return instance
        }
    }

    init {
        val builder = Retrofit.Builder()
        //postavljanje retrofit-a
        val retrofit = builder.baseUrl("https://api.edamam.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(ApiService::class.java)
    }
}