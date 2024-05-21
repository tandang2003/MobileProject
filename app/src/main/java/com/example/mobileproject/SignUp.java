package com.example.mobileproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SignUp extends BottomSheetDialog {
    public SignUp(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Sign Up Clicked");
        setContentView(R.layout.login_bottom_dialog);
    }
}
