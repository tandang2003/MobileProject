package com.example.mobileproject.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileproject.R;
import com.example.mobileproject.fragment.ExploreFragment;
import com.example.mobileproject.fragment.LibraryFragment;
import com.example.mobileproject.fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LibraryActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;

    private GestureDetector gestureDetector;

    private int SWIPE_THRESHOLD = 100;
    private int SWIPE_VELOCITY_THRESHOLD = 100;
    private int currentFragmentIndex = 0; // Chỉ mục của fragment hiện tại

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        frameLayout = findViewById(R.id.frameLayout);

        gestureDetector = new GestureDetector(this, new MyGesture());
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_store) {
                    currentFragmentIndex = 0;
                } else if (itemId == R.id.navigation_library) {
                    currentFragmentIndex = 1;
                } else if (itemId == R.id.navigation_setting) {
                    currentFragmentIndex = 2;
                }
                loadFragment(getFragmentByIndex(currentFragmentIndex), false);
                return true;
            }
        });
        // Load fragment mặc định
        loadFragment(getFragmentByIndex(currentFragmentIndex), true);
    }

    // Phương thức này trả về fragment tương ứng với chỉ mục được chỉ định
    private Fragment getFragmentByIndex(int index) {
        switch (index) {
            case 0:
                return new ExploreFragment();
            case 1:
                return new LibraryFragment();
            case 2:
                return new SettingFragment();
            default:
                return new ExploreFragment(); // Trả về fragment khởi đầu nếu không có fragment nào tương ứng
        }
    }

    // Phương thức này dùng để load fragment vào frameLayout
    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.commit();
    }

    // Bộ lắng nghe sự kiện vuốt
    class MyGesture extends GestureDetector.SimpleOnGestureListener {
        public boolean onFling(MotionEvent e1, MotionEvent e2, float x, float y) {
            if (e2.getX() - e1.getX() > SWIPE_THRESHOLD && Math.abs(x) > SWIPE_VELOCITY_THRESHOLD) {
                // Vuốt từ trái sang phải
                if (currentFragmentIndex > 0) {
                    currentFragmentIndex--;
                    loadFragment(getFragmentByIndex(currentFragmentIndex), false);
                    bottomNavigationView.getMenu().getItem(currentFragmentIndex).setChecked(true);
                }
            } else if (e1.getX() - e2.getX() > SWIPE_THRESHOLD && Math.abs(x) > SWIPE_VELOCITY_THRESHOLD) {
                // Vuốt từ phải sang trái
                if (currentFragmentIndex < 2) {
                    currentFragmentIndex++;
                    loadFragment(getFragmentByIndex(currentFragmentIndex), false);
                    bottomNavigationView.getMenu().getItem(currentFragmentIndex).setChecked(true);
                }
            }
            return super.onFling(e1, e2, x, y);
        }
    }
}
