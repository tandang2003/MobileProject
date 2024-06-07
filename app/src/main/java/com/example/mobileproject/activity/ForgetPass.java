package com.example.mobileproject.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.mobileproject.R;

public class ForgetPass extends Dialog {
    public ForgetPass(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.getWindow().setLayout(1000, 1000);
        setContentView(R.layout.forget_pass_dialog);
    }
}
