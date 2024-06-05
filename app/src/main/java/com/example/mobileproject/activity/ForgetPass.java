package com.example.mobileproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class ForgetPass extends Dialog {
    public ForgetPass(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Forget Password Clicked");
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.getWindow().setLayout(1000, 1000);
        setContentView(R.layout.forget_pass_dialog);
    }
}
