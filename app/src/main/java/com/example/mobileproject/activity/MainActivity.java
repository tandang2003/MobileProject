package com.example.mobileproject.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mobileproject.sharedPreference.GetData;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra token đăng nhập
        String token = GetData.getInstance().getString("token");
        System.out.println("123" + token);
        if (token != null && !token.isEmpty()) {
            // Người dùng đã đăng nhập, chuyển đến LibraryActivity
            Intent intent = new Intent(MainActivity.this, LibraryActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Người dùng chưa đăng nhập, chuyển đến LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
