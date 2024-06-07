package com.example.mobileproject.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    Retrofit apiService = new Retrofit.Builder()
            .baseUrl("https://192.168.0.10:8080/ebook/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
}
