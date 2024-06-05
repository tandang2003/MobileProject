package com.example.mobileproject.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mobileproject.R;

public class Error extends Dialog {
    private String notify;
    public Error(@NonNull Context context, String notify) {
        super(context);
        this.notify= notify;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.getWindow().setLayout(1000, 1000);
        setContentView(R.layout.error_dialog);
        System.out.println(notify);
        TextView textView= findViewById(R.id.notify);
        textView.setText(notify);
        TextView cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(view -> dismiss());
    }

}
