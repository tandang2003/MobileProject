package com.example.mobileproject.api;

import com.example.mobileproject.dto.request.ChangeStatusBookRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.BookResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiAdmin extends ApiService {
    @GET("admin/books")
    Call<ApiResponse<List<BookResponse>>> getBooks(@Header("Authorization") String token);

    @POST("admin/books/status")
    Call<ApiResponse<Void>> changeBookStatus(@Header("Authorization") String token, @Body ChangeStatusBookRequest request);
}
