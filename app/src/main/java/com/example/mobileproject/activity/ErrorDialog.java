package com.example.mobileproject.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mobileproject.R;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ErrorDialog extends Dialog {
    private String notify;
    private String btn,title;
    public ErrorDialog(@NonNull Context context, String notify) {
        super(context);
        this.notify= notify;
        this.btn= "cancel";
        this.title="Error";

    }
    public ErrorDialog(@NonNull Context context, String notify, String btn, String title) {
        super(context);
        this.notify= notify;
        this.btn= btn;
        this.title=title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.getWindow().setLayout(1000, 1000);
        setContentView(R.layout.error_dialog);
        TextView textView= findViewById(R.id.notify);
        textView.setText(notify);
        TextView cancel = findViewById(R.id.btn);
        cancel.setText(btn);
        TextView titleView = findViewById(R.id.title);
        titleView.setText(title);

        cancel.setOnClickListener(view -> dismiss());
    }

}
