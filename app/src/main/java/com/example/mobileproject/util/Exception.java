package com.example.mobileproject.util;

import android.content.Context;

public enum Exception {
    NOT_FOUND_DATA(404, "Data not found", Context.MODE_PRIVATE),
    FAILURE_CALL_API(500, "Error system please try again later", Context.MODE_PRIVATE),
    UNAUTHORIZED(401, "Invalid Email or Password", Context.MODE_PRIVATE),
    INVALID_EMAIL(400, "Invalid Email", Context.MODE_PRIVATE),
    INVALID_NAME(400, "Please enter your name", Context.MODE_PRIVATE),
    INVALID_PASSWORD(400, "Invalid Password", Context.MODE_PRIVATE),
    NOT_IMAGE_SELECTED(400, "Please select image", Context.MODE_PRIVATE),
    INVALID_CONFIRM_PASSWORD(400, "Invalid Confirm Password", Context.MODE_PRIVATE),
    SUBMIT_FAILURE(400, "Please try again later", Context.MODE_PRIVATE),
    GET_USER_FAILURE(400, "The process is failure please try again later", Context.MODE_PRIVATE) ;
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
