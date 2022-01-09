package com.example.pma_kuharica.interfaces;

import com.example.pma_kuharica.classes.Hint;
import com.example.pma_kuharica.classes.HintsResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface ApiService {
    @GET
    public Call<HintsResults> getFood(@Url String url);
}
