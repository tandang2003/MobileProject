package com.example.mobileproject.activity.auth;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.R;
import com.example.mobileproject.activity.ErrorDialog;
import com.example.mobileproject.api.ApiAuthentication;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dialog.VerifyDialog;
import com.example.mobileproject.dto.request.ForgetPasswordRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.util.Exception;
import com.example.mobileproject.util.Util;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    private TextInputEditText email;
    private MaterialButton button;

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pass_activity);
        email = findViewById(R.id.email);
        button = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backBtn);
        button.setOnClickListener(view -> {
            forgetPassword(email.getText().toString());
        });
        backButton.setOnClickListener(view -> {
            finish();
        });
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void forgetPassword(String email) {
        if (validateEmail(email)) return;
        ApiService.apiService.create(ApiAuthentication.class)
                .forgetPassword(ForgetPasswordRequest.
                        builder().
                        email(email).
                        build()
                ).enqueue(new Callback<ApiResponse<Void>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                        System.out.println(response.body().toString());
                        if (response.isSuccessful()) {
//                            Bundle bundle = new Bundle();
//                            bundle.putString("email", email);
//                            Intent intent = new Intent(ForgetPasswordActivity.this, VerifyActivity.class);
//                            intent.putExtra("email", bundle);
//                            startActivity(intent);

                            VerifyDialog verifyActivity=new VerifyDialog(ForgetPasswordActivity.this,email);
                            verifyActivity.show();
//                            Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            ApiResponse<?> apiResponse = Util.getInstance().convertErrorBody(response.errorBody());
                            Toast.makeText(ForgetPasswordActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Void>> call, Throwable throwable) {
                        System.out.println(throwable.getMessage()+"____________________"+ throwable.getLocalizedMessage());
                        ErrorDialog dialog = new ErrorDialog(ForgetPasswordActivity.this, Exception.FAILURE_CALL_API.getMessage());
                        dialog.show();
                    }
                });
    }
}
