package com.example.mobileproject.util;

import android.content.Context;

public enum Exception {
    NOT_FOUND_DATA(404, "Data not found", Context.MODE_PRIVATE),
    FAILURE_CALL_API(500, "Error system please try again later", Context.MODE_PRIVATE),
    UNAUTHORIZED(401, "Invalid Email or Password", Context.MODE_PRIVATE),
    ;
    private String message;
    private int code;
    private int status;

    Exception(int code, String message, int status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}