package com.example.mobileproject.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileproject.R;
import com.example.mobileproject.activity.ErrorDialog;
import com.example.mobileproject.activity.LibraryActivity;
import com.example.mobileproject.api.ApiAuthentication;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dialog.auth.SignUpDialog;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.AuthenticationResponse;
import com.example.mobileproject.model.AuthenticationRequest;
import com.example.mobileproject.sharedPreference.GetData;
import com.example.mobileproject.util.Exception;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.GetTokenResult;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    TextInputEditText email, password;
    TextInputLayout emailLayout, passwordLayout;
    MaterialButton loginButton, googleSignInButton;
    TextView signUp, forgetPassButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        email = findViewById(R.id.emailLoginInput);
        password = findViewById(R.id.passwordLoginInput);
        emailLayout = findViewById(R.id.emailLoginInputLayout);
        passwordLayout = findViewById(R.id.passwordLoginInputLayout);
        loginButton = findViewById(R.id.loginButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        signUp = findViewById(R.id.signUpButton);
        forgetPassButton = findViewById(R.id.forgotPasswordButton);

        // Set up button listeners
        loginButton.setOnClickListener(view -> authenticateWithEmailPassword());
        googleSignInButton.setOnClickListener(view -> signInWithGoogle());
        signUp.setOnClickListener(view -> showSignUpDialog());
        forgetPassButton.setOnClickListener(view -> navigateToForgotPassword());

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, go to LibraryActivity
            goToLibraryActivity();
        }
    }

    private void signInWithGoogle() {
        // Sign out before attempting to sign in again
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // Start the Google sign-in intent
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            // Handle Google Sign In failed
            ErrorDialog error = new ErrorDialog(LoginActivity.this, "Google sign-in failed.");
            error.show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Retrieve the Firebase ID token
                            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                @Override
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {
                                        // Get ID token
                                        String idToken = task.getResult().getToken();
                                        // Store the token
                                        GetData.getInstance().setString("token", idToken);
                                        // Navigate to LibraryActivity after successful login
                                        goToLibraryActivity();
                                    } else {
                                        Log.w(TAG, "getIdToken:failure", task.getException());
                                        ErrorDialog error = new ErrorDialog(LoginActivity.this, "Failed to get ID token.");
                                        error.show();
                                    }
                                }
                            });
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        ErrorDialog error = new ErrorDialog(LoginActivity.this, "Firebase Authentication failed.");
                        error.show();
                    }
                });
    }


    private void authentication() {
        Log.i("LoginActivity", "authentication_______________________");
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
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
                        Log.i("LoginActivity", response.toString());
                        if (response.isSuccessful()) {
                            Log.i("LoginActivity", response.body().toString());
                            ApiResponse<AuthenticationResponse> authenticationResponse = response.body();
                            AuthenticationResponse result = authenticationResponse.getResult();
                            if (result.isAuthenticated()) {
                                GetData.getInstance().setString("token", result.getToken());

                                // Example: Navigate to LibraryActivity after successful login
                                goToLibraryActivity();

                            } else {
                                ErrorDialog error = new ErrorDialog(LoginActivity.this, Exception.UNAUTHORIZED.getMessage());
                                error.show();
                            }
                        } else {
                            ErrorDialog error = new ErrorDialog(LoginActivity.this, Exception.FAILURE_CALL_API.getMessage());
                            error.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<AuthenticationResponse>> call, Throwable t) {

                        Log.e(TAG, "Authentication API call failed", t);

                        ErrorDialog errorDialog = new ErrorDialog(LoginActivity.this, Exception.FAILURE_CALL_API.getMessage());
                        errorDialog.show();
                    }
                });

    }

    private void showSignUpDialog() {
        SignUpDialog signUpDialog = new SignUpDialog(LoginActivity.this);
        signUpDialog.show();
    }

    private void navigateToForgotPassword() {
        Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
        startActivity(intent);

    }

    private void goToLibraryActivity() {
        // Example: Navigate to LibraryActivity after successful login
        Intent intent = new Intent(LoginActivity.this, LibraryActivity.class);
        startActivity(intent);
        finish();
    }
}
