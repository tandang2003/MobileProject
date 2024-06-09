package com.example.mobileproject.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();
    Retrofit apiService = new Retrofit.Builder()
            .baseUrl("https://a056-118-70-31-97.ngrok-free.app/ebook/")
//            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();



}
