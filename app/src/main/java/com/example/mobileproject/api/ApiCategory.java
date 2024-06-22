package com.example.mobileproject.api;

import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.BookResponse;
import com.example.mobileproject.dto.response.CategoryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiCategory extends ApiService {
    @GET("categories")
    Call<ApiResponse<List<CategoryResponse>>> getCategories();

    @GET("categories/{id}")
    Call<ApiResponse<List<BookResponse>>> getCategoryById(@Path("id") long id);
}
