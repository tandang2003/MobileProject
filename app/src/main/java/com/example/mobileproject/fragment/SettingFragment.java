package com.example.mobileproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileproject.R;
import com.example.mobileproject.activity.EditProfileActivity;
import com.example.mobileproject.activity.auth.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.example.mobileproject.activity.ErrorDialog;
import com.example.mobileproject.activity.admin.BookManagerActivity;
import com.example.mobileproject.activity.auth.LoginActivity;
import com.example.mobileproject.api.ApiAdmin;
import com.example.mobileproject.api.ApiAuthentication;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.request.LogoutRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.RoleResponse;
import com.example.mobileproject.sharedPreference.GetData;
import com.example.mobileproject.util.Exception;
import com.example.mobileproject.util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment {
    private TableRow rowUserInfo, rowLogout, rowBookManager;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Initialize Firebase Auth and Google Sign-In Client
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN);

        // Find TableRow elements by their IDs
        rowUserInfo = view.findViewById(R.id.row_user_info);
        rowLogout = view.findViewById(R.id.row_logout);
        rowBookManager = view.findViewById(R.id.row_book_manager);

        // Set click listeners for each TableRow
        rowUserInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        checkingRole();

        rowLogout.setOnClickListener(v -> {

            // Sign out from Firebase Auth
            mAuth.signOut();

            // Sign out from Google Sign-In
            mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), task -> {
                // After sign out, navigate to LoginActivity or your desired destination
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            });

        });


        return view;
    }

    private void checkingRole() {
        String token = Util.getInstance().getToken();
        ApiService.apiService.create(ApiAuthentication.class)
                .getRole(token)
                .enqueue(new Callback<ApiResponse<RoleResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<RoleResponse>> call, Response<ApiResponse<RoleResponse>> response) {
                        if (response.isSuccessful()) {

                            RoleResponse roleResponse = response.body().getResult();
                            if (roleResponse.getName().equals("ADMIN")) {
                                rowBookManager.setVisibility(View.VISIBLE);
                                rowBookManager.setOnClickListener(v -> {
                                    Intent intent = new Intent(getActivity(), BookManagerActivity.class);
                                    startActivity(intent);
                                });
                            }
                        } else {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            rowBookManager.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<RoleResponse>> call, Throwable throwable) {
                        ErrorDialog errorDialog = new ErrorDialog(getContext(), Exception.FAILURE_CALL_API.getMessage());
                        errorDialog.show();
                        rowBookManager.setVisibility(View.GONE);
                    }
//                    @Override
//                    public void onResponse(Call<RoleResponse> call, Response<RoleResponse> response) {
//                        if (response.isSuccessful()) {
//                            RoleResponse roleResponse = response.body();
//                            if (roleResponse.getName().equals("ROLE_ADMIN")) {
//                                rowBookManager.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<RoleResponse> call, Throwable throwable) {
//                        ErrorDialog errorDialog = new ErrorDialog(getContext(), Exception.FAILURE_CALL_API.getMessage());
//                        errorDialog.show();
//                    }
                });
    }
}
