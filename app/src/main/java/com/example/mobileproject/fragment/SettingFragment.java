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

public class SettingFragment extends Fragment {

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
        TableRow rowUserInfo = view.findViewById(R.id.row_user_info);
        TableRow rowMemoryManagement = view.findViewById(R.id.row_memory_management);
        TableRow rowTheme = view.findViewById(R.id.row_theme);
        TableRow rowLogout = view.findViewById(R.id.row_logout);

        // Set click listeners for each TableRow
        rowUserInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        rowMemoryManagement.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Quản lý bộ nhớ clicked", Toast.LENGTH_SHORT).show();
            // Handle the event for memory management row click
        });

        rowTheme.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chủ đề clicked", Toast.LENGTH_SHORT).show();
            // Handle the event for theme row click
        });

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
}
