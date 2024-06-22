package com.example.mobileproject.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mobileproject.R;
import com.example.mobileproject.api.ApiAuthentication;
import com.example.mobileproject.api.ApiService;
import com.example.mobileproject.dto.request.UserUpdationRequest;
import com.example.mobileproject.dto.response.ApiResponse;
import com.example.mobileproject.dto.response.UserResponse;
import com.example.mobileproject.sharedPreference.GetData;
import com.example.mobileproject.util.Exception;
import com.example.mobileproject.util.Util;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfileActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private ImageView imgAvatar, backBtn;
    private TextView selectAvatar;
    private TextInputEditText txtUsername, txtEmail;
    private Button submit;
    private StorageReference storageReference;
    private LinearProgressIndicator progress;
    private Uri img;
    private String urlUpload;
    private final String urlFirebaseStorage = "gs://ebook-c86ab.appspot.com/";

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    img = result.getData().getData();
                    selectAvatar.setEnabled(true);
                    Glide.with(EditProfileActivity.this).load(img).into(imgAvatar);
                }
            } else {
                Toast.makeText(EditProfileActivity.this, Exception.NOT_IMAGE_SELECTED.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile); // Assuming you keep the layout name the same
        //get token
        FirebaseApp.initializeApp(EditProfileActivity.this);
        storageReference = FirebaseStorage.getInstance().getReference();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progress = findViewById(R.id.progress);
        imgAvatar = findViewById(R.id.imgAvatar);
        backBtn = findViewById(R.id.backBtn);
        selectAvatar = findViewById(R.id.selectAvatar);
        submit = findViewById(R.id.submit);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        getUser();

        selectAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
        });

        submit.setOnClickListener(v -> {
            submit(img, txtUsername.getText().toString());
        });
        backBtn.setOnClickListener(v -> {
            EditProfileActivity.this.finish();
        });
    }

    private void submit(Uri img, String username) {
        System.out.println("submit");
        if (img == null) {
            Toast.makeText(EditProfileActivity.this, Exception.NOT_IMAGE_SELECTED.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.isEmpty()) {
            txtUsername.setError(Exception.INVALID_NAME.getMessage());
            return;
        }
        boolean flag = upload(img);
        System.out.println("flag " + flag);
        if (flag) {
            Toast.makeText(EditProfileActivity.this, Exception.SUBMIT_FAILURE.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean upload(Uri img) {
        boolean[] flag = {false};
        StorageReference reference = storageReference.child("images/avatar");
        reference.putFile(img).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        flag[0] = downUrl(taskSnapshot);
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull java.lang.Exception e) {
                progress.setVisibility(View.GONE);
                Toast.makeText(EditProfileActivity.this, Exception.SUBMIT_FAILURE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                flag[0] = false;
                Toast.makeText(EditProfileActivity.this, Exception.SUBMIT_FAILURE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(uploadProgress());
        System.out.println("check flag");
        Log.i("EditProfileActivity", "upload status flag: " + flag[0]);
        System.out.println("check flag");
        System.out.println("flag[0] " + flag[0]);
        return flag[0];
    }

    // can run
    private void getUser() {
        String header = Util.getInstance().getToken();
        ApiService.apiService.create(ApiAuthentication.class)
                .getUserInfo(header).enqueue(new Callback<ApiResponse<UserResponse>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                        if (response.isSuccessful()) {
                            ApiResponse<UserResponse> userResponse = response.body();
                            UserResponse user = userResponse.getResult();
                            txtUsername.setText(user.getFullName());
                            Glide.with(EditProfileActivity.this).load(user.getAvatar()).into(imgAvatar);
                            txtEmail.setText(user.getEmail());
                        } else {
                            Toast.makeText(EditProfileActivity.this, Exception.GET_USER_FAILURE.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable throwable) {
                        System.out.println("response: " + call.toString());
                        throwable.getMessage();
                    }
                });
    }


    private OnProgressListener<UploadTask.TaskSnapshot> uploadProgress() {
        return new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progress.setVisibility(View.VISIBLE);
                progress.setMax(Math.toIntExact(snapshot.getTotalByteCount()));
                progress.setProgress(Math.toIntExact(snapshot.getBytesTransferred()));
            }
        };
    }

    public boolean downUrl(UploadTask.TaskSnapshot taskSnapshot) {
        boolean[] flag = {false};
        taskSnapshot.getStorage().getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        urlUpload = uri.toString();
                        updateUser(urlUpload);
                        progress.setProgress(0);
                        progress.setVisibility(View.GONE);
                        flag[0] = true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull java.lang.Exception e) {
                        flag[0] = false;
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        flag[0] = false;
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        flag[0] = true;
                    }
                });
        return flag[0];
    }

    private void updateUser(String urlUpload) {
        ApiService.apiService.create(ApiAuthentication.class)
                .updateUser("Bearer " + GetData.getInstance().getString("token"), UserUpdationRequest.
                        builder().avatar(urlUpload).fullName(txtUsername.getText().toString()).
                        build()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.i("EditProfileActivity", "response: " + response.toString());
                        if (response.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "Update success", Toast.LENGTH_SHORT).show();
//                                finish();
                        } else {
                            Toast.makeText(EditProfileActivity.this, Exception.SUBMIT_FAILURE.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        Toast.makeText(EditProfileActivity.this, Exception.SUBMIT_FAILURE.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}