package com.example.mobileproject.util;

import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.response.ApiResponse;

import okhttp3.ResponseBody;

public class Util {
    private static Util instance;
    public Util() {
    }
    public static Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }
    public  ApiResponse<?> convertErrorBody(ResponseBody responseBody) {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse = ApiService.gson.fromJson(responseBody.charStream(), apiResponse.getClass());
        return apiResponse;
    }
}
