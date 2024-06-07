package com.example.mobileproject.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.R;
import com.example.mobileproject.api.ApiAuthentication;
import com.example.mobileproject.api.ApiBook;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.AuthenticationResponse;
import com.example.mobileproject.dto.response.BookResponse;
import com.example.mobileproject.model.AuthenticationRequest;
import com.example.mobileproject.sharedPreference.GetData;
import com.example.mobileproject.util.Exception;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

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
        setContentView(R.layout.login_page);
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
                SignUp signUp = new SignUp(LoginActivity.this);
                signUp.show();
            }
        });
        forgetPassButton = (MaterialButton) findViewById(R.id.forgotPasswordButton);
        forgetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgetPass forgetPass = new ForgetPass(LoginActivity.this);
                forgetPass.show();
            }
        });

    }
    private void book(){
        ApiService.apiService.create(ApiBook.class).getBooks().enqueue(new Callback<ApiResponse<List<BookResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BookResponse>>> call, Response<ApiResponse<List<BookResponse>>> response) {

            }

            @Override
            public void onFailure(Call<ApiResponse<List<BookResponse>>> call, Throwable throwable) {

            }
        });
    }

    private void authentication() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(emailText, passwordText);
        ApiService.apiService.create(ApiAuthentication.class)
                .authenticate(authenticationRequest)
                .enqueue(new Callback<ApiResponse<AuthenticationResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthenticationResponse>> call, Response<ApiResponse<AuthenticationResponse>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<AuthenticationResponse> authenticationResponse = response.body();
                    AuthenticationResponse result = authenticationResponse.getResult();
                    if(result.isAuthenticated()){
                        GetData.getInstance().setString("token", result.getToken());
                        System.out.println(GetData.getInstance().getString("token"));
                        //TODO:change to home page
                    } else {
                        Error error = new Error(LoginActivity.this, Exception.UNAUTHORIZED.getMessage());
                        error.show();
                    }
                } else {
                    Error error = new Error(LoginActivity.this, Exception.FAILURE_CALL_API.getMessage());
                    error.show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable t) {
                Error error = new Error(LoginActivity.this, Exception.FAILURE_CALL_API.getMessage());
                error.show();
            }
        });
    }

}
