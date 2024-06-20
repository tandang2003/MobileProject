package com.example.mobileproject.api;

import com.example.mobileproject.dto.request.ForgetPasswordRequest;
import com.example.mobileproject.dto.request.LogoutRequest;
import com.example.mobileproject.dto.request.ResetPasswordRequest;
import com.example.mobileproject.dto.request.UserCreationRequest;
import com.example.mobileproject.dto.request.UserUpdationRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.AuthenticationResponse;
import com.example.mobileproject.dto.response.RoleResponse;
import com.example.mobileproject.dto.response.UserResponse;
import com.example.mobileproject.model.AuthenticationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiAuthentication extends ApiService {
    //    @FormUrlEncoded
    @POST("auth/token")
    Call<ApiResponse<AuthenticationResponse>> authenticate(@Body AuthenticationRequest authenticationRequest);

    @POST("users")
    Call<ApiResponse<UserResponse>> register(@Body UserCreationRequest userRegisterRequest);

    @POST("users/forget-password")
    Call<ApiResponse<Void>> forgetPassword(@Body ForgetPasswordRequest forgetPasswordRequest);

    @POST("users/reset-password")
    Call<ApiResponse<Void>> resetPassword(@Body ResetPasswordRequest request);

    @POST("auth/logout")
    Call<ApiResponse<Void>> logout(@Body LogoutRequest logoutRequest);

    @GET("users/myInfo")
    Call<ApiResponse<UserResponse>> getUserInfo(@Header("Authorization") String token);

    @PUT("users/update")
    Call<Void> updateUser(@Header("Authorization") String token, @Body UserUpdationRequest userUpdationRequest);

    @GET("users/role")
    Call<ApiResponse<RoleResponse>> getRole(@Header("Authorization") String token);
}