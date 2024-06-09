package com.example.mobileproject.api;

import com.example.mobileproject.dto.request.ForgetPasswordRequest;
import com.example.mobileproject.dto.request.ResetPasswordRequest;
import com.example.mobileproject.dto.request.UserCreationRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.AuthenticationResponse;
import com.example.mobileproject.dto.response.UserResponse;
import com.example.mobileproject.model.AuthenticationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiAuthentication extends ApiService{
//    @FormUrlEncoded
    @POST("auth/token")
    Call<ApiResponse<AuthenticationResponse>>authenticate(@Body AuthenticationRequest authenticationRequest);

    @POST("users")
    Call<ApiResponse<UserResponse>>register(@Body UserCreationRequest userRegisterRequest);

    @POST("users/forget-password")
    Call<ApiResponse<Void>>forgetPassword(@Body ForgetPasswordRequest forgetPasswordRequest);
    @POST("users/reset-password")
    Call<ApiResponse<Void>>resetPassword(@Body ResetPasswordRequest request);
}
