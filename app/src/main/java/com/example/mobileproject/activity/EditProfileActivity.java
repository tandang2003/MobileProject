package com.example.mobileproject.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfileActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private ImageView imgAvatar;
    private TextView selectAvatar;
    private TextInputEditText txtUsername, txtEmail;
    private Button submit;
    private StorageReference storageReference;
    private LinearProgressIndicator progress;
    private Uri img;
    private String urlUpload;
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

        FirebaseApp.initializeApp(EditProfileActivity.this);
        storageReference = FirebaseStorage.getInstance().getReference();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progress = findViewById(R.id.progress);
        imgAvatar = findViewById(R.id.imgAvatar);
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
            upload(img);
        });
    }

    private void submit(Uri img, String username) {
        if (img == null) {
            Toast.makeText(EditProfileActivity.this, Exception.NOT_IMAGE_SELECTED.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.isEmpty()) {
            txtUsername.setError(Exception.INVALID_NAME.getMessage());
            return;
        }
        if (!upload(img)) {
            Toast.makeText(EditProfileActivity.this, Exception.SUBMIT_FAILURE.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            //Todo call api
        ApiService.apiService.create(ApiAuthentication.class)
                .updateUser("Bearer " + GetData.getInstance().getString("token"), UserUpdationRequest.
                        builder().avatar(urlUpload).fullName(username).
                        build()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                    finish();
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

    private boolean upload(Uri img) {
        final boolean[] flag = {false};
        StorageReference reference = storageReference.child("images/avatar/" + img.getLastPathSegment());
        reference.putFile(img).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final boolean[] innerFlage = {flag[0]};
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                innerFlage[0] = true;
                                urlUpload = uri.toString();
                                Toast.makeText(EditProfileActivity.this, "Upload success", Toast.LENGTH_SHORT).show();
                                progress.setProgress(0);
                                progress.setVisibility(View.GONE);
                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                innerFlage[0] = false;
                                Toast.makeText(EditProfileActivity.this, Exception.SUBMIT_FAILURE.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull java.lang.Exception e) {
                                innerFlage[0] = false;
                                Toast.makeText(EditProfileActivity.this, Exception.SUBMIT_FAILURE.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        flag[0] = innerFlage[0];
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull java.lang.Exception e) {
                flag[0] = false;

                progress.setVisibility(View.GONE);
                Toast.makeText(EditProfileActivity.this, Exception.SUBMIT_FAILURE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                flag[0] = false;
                Toast.makeText(EditProfileActivity.this, Exception.SUBMIT_FAILURE.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progress.setVisibility(View.VISIBLE);
                progress.setMax(Math.toIntExact(snapshot.getTotalByteCount()));
                progress.setProgress(Math.toIntExact(snapshot.getBytesTransferred()));
            }
        });
        return flag[0];
    }

    private void getUser() {
        String t = GetData.getInstance().getString("token");
        String header = "Bearer " + t;
//        ApiService.apiService.create(ApiBook.class).getBooks().enqueue(new Callback<ApiResponse<List<BookResponse>>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<List<BookResponse>>> call, Response<ApiResponse<List<BookResponse>>> response) {
//                System.out.println("response: " + response.body());
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<List<BookResponse>>> call, Throwable throwable) {
//
//            }
//        });
        System.out.println("start");
        ApiService.apiService.create(ApiAuthentication.class)
                .getUserInfo(header).enqueue(new Callback<ApiResponse<UserResponse>>() {

                    @Override
                    public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                        System.out.println("response: " + response.body());
                        if (response.isSuccessful()) {
                            ApiResponse<UserResponse> userResponse = response.body();
                            UserResponse user = userResponse.getResult();
                            txtUsername.setText(user.getFullName());
                            Glide.with(EditProfileActivity.this).load(user.getAvatar()).into(imgAvatar);
                            txtEmail.setText(user.getEmail());
                        }else{
                            Toast.makeText(EditProfileActivity.this, Exception.GET_USER_FAILURE.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable throwable) {

                    }
                });
    }
}
