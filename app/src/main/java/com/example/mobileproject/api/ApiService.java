package com.example.mobileproject.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    Retrofit apiService = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/ebook/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();



}
