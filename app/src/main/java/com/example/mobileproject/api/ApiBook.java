package com.example.mobileproject.api;

import com.example.mobileproject.dto.request.WishListRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.BookResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBook extends ApiService {
    @GET("books")
    Call<ApiResponse<List<BookResponse>>> getBooks();

    @POST("wish-list")
    Call<ApiResponse<Void>> addToWishlist(@Body WishListRequest request);
}
