package com.example.mobileproject.api;

import com.example.mobileproject.dto.request.WishListRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.BookResponse;
import com.example.mobileproject.dto.response.WishListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiBook extends ApiService {
    @GET("books")
    Call<ApiResponse<List<BookResponse>>> getBooks();
    @GET("/wish-list")
    Call<ApiResponse<List<BookResponse>>> getBooksInWishList();

    @POST("/wish-list")
    Call<ApiResponse<WishListResponse>> addToWishlist(@Header("Authorization") String token, @Body WishListRequest request);

    @DELETE("/wish-list/{bookId}")
    Call<ApiResponse<Void>> removeFromWishlist(@Path("bookId") Long bookId);
}
