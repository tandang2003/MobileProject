package com.example.mobileproject.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.R;
import com.example.mobileproject.activity.ErrorDialog;
import com.example.mobileproject.dialog.auth.SignUpDialog;
import com.example.mobileproject.api.ApiAuthentication;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.AuthenticationResponse;
import com.example.mobileproject.model.AuthenticationRequest;
import com.example.mobileproject.sharedPreference.GetData;
import com.example.mobileproject.util.Exception;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView signUp;
    TextInputEditText email, password;
    TextInputLayout emailLayout, passwordLayout;
    MaterialButton loginButton;
    MaterialButton forgetPassButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        email = findViewById(R.id.emailLoginInput);
        password = findViewById(R.id.passwordLoginInput);
        loginButton = findViewById(R.id.loginButton);
        emailLayout = findViewById(R.id.emailLoginInputLayout);
        passwordLayout = findViewById(R.id.passwordLoginInputLayout);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authentication();
            }
        });

        signUp = (TextView) findViewById(R.id.signUpButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpDialog signUp = new SignUpDialog(LoginActivity.this);
                signUp.show();
            }
        });
        forgetPassButton = (MaterialButton) findViewById(R.id.forgotPasswordButton);
        forgetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void authentication() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(emailText, passwordText);
        ApiService.apiService.create(ApiAuthentication.class)
                .authenticate(AuthenticationRequest
                        .builder()
                        .email(emailText)
                        .password(passwordText)
                        .build()
                )
                .enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, Response<ApiResponse<AuthenticationResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<AuthenticationResponse> authenticationResponse = response.body();
                    AuthenticationResponse result = authenticationResponse.getResult();
                    if(result.isAuthenticated()){
                        GetData.getInstance().setString("token", result.getToken());
                        //TODO:change to home page
                    } else {
                        ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, Exception.UNAUTHORIZED.getMessage());
                        errorDialog.show();
                    }
                } else {
                    ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, Exception.FAILURE_CALL_API.getMessage());
                    errorDialog.show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable t) {
                ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, Exception.FAILURE_CALL_API.getMessage());
                errorDialog.show();
            }
        });
    }

}
