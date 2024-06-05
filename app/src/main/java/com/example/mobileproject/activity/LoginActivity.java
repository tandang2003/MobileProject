package com.example.mobileproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
        System.out.println("Login Activity");
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
                if (false) {
                    System.out.println("Login Success");
                }else{
                    email.setError("Unexists");
                }
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

}
