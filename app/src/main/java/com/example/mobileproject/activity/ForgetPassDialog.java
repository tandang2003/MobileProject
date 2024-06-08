package com.example.mobileproject.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.mobileproject.R;
import com.example.mobileproject.api.ApiAuthentication;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.UserResponse;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassDialog extends Dialog {
    private TextInputEditText email;

    public ForgetPassDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.getWindow().setLayout(1000, 1000);
        setContentView(R.layout.forget_pass_dialog);

        email = findViewById(R.id.passwordLoginInput);

        email.setOnClickListener(view -> {
            String emailText = this.email.getText().toString();
            //Email validation
            if (!emailText.isEmpty() && emailText.contains("@") && emailText.contains(".")) {
                forgetPassword(emailText);
            } else {
                ErrorDialog errorDialog = new ErrorDialog(getContext(), "Invalid email");
                errorDialog.show();
            }

        });
    }

    private void forgetPassword(String email) {
        ApiService.apiService.create(ApiAuthentication.class).forgetPassword(email).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable throwable) {

            }
        });
    }
}
