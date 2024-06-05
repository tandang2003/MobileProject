package com.example.mobileproject.api;

import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.BookResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiBook extends ApiService {
    @GET("books")
    Call<ApiResponse<List<BookResponse>>> getBook();
}
