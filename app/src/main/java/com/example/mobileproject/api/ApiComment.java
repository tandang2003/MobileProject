package com.example.mobileproject.api;

import com.example.mobileproject.dto.request.CommentRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.CommentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiComment {
    @POST("comments")
    Call<ApiResponse<CommentResponse>> addComment(@Header("Authorization") String token, @Body CommentRequest comment);
}
