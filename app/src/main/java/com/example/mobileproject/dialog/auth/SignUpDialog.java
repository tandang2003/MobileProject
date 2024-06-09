package com.example.mobileproject.dialog.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.mobileproject.R;
import com.example.mobileproject.activity.ErrorDialog;
import com.example.mobileproject.api.ApiAuthentication;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.request.UserCreationRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.UserResponse;
import com.example.mobileproject.util.Exception;
import com.example.mobileproject.util.Util;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpDialog extends BottomSheetDialog {
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private MaterialButton signUpButton;
    private Context context;

    public SignUpDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_bottom_dialog);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void signUp() {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String confirmPassword = this.confirmPassword.getText().toString();
        if (!checkingValidation(email, password, confirmPassword)) {
            return;
        }
        ApiService.apiService.create(ApiAuthentication.class)
                .register(
                        UserCreationRequest.builder()
                                .email(email)
                                .password(password)
                                .confirmPassword(confirmPassword)
                                .build())
                .enqueue(new Callback<ApiResponse<UserResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                        if (response.isSuccessful()) {
                            ApiResponse<UserResponse> apiResponse = response.body();
                            if (apiResponse.getCode() == 1000) {
                                UserResponse userResponse = apiResponse.getResult();
                                if (userResponse != null) {
                                    showAlert();
                                }
                            } else {
                                ErrorDialog errorDialog = new ErrorDialog(context, apiResponse.getMessage());
                                errorDialog.show();
                            }
                        } else {
                            ApiResponse<?> apiResponse = Util.getInstance().convertErrorBody(response.errorBody());
                            ErrorDialog errorDialog = new ErrorDialog(context, apiResponse.getMessage());
                            errorDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable
                            throwable) {
                        ErrorDialog errorDialog = new ErrorDialog(context, Exception.FAILURE_CALL_API.getMessage());
                        errorDialog.show();
                    }
                });
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Sign up success")
                .setMessage("Welcome to EBook!")
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finishActivity();
                })
                .create();
        alertDialog.show();
    }

    private void finishActivity() {
        dismiss();
    }

    public boolean checkingValidation(String email, String password, String confirmPassword) {
        if (email.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty() ||
                !password.equals(confirmPassword) || !email.contains("@")) {
            ErrorDialog errorDialog = null;
            if (email.isEmpty() || !email.contains("@")) {
                errorDialog = new ErrorDialog(context, Exception.INVALID_EMAIL.getMessage());
            } else if (password.isEmpty()) {
                errorDialog = new ErrorDialog(context, Exception.INVALID_PASSWORD.getMessage());
            } else if (confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
                errorDialog = new ErrorDialog(context, Exception.INVALID_CONFIRM_PASSWORD.getMessage());
            }
            errorDialog.show();
            return false;
        }
        return true;

    }
}
