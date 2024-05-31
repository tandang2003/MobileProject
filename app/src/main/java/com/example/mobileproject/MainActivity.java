package com.example.mobileproject;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.library);
//    }
    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;

    private GestureDetector gestureDetector;

    private  int SWIPE_THRESHOLD = 100;

    private int SWIPE_VELOCITY_THRESHOLD=100;

    private int number_Menu;

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

                if (itemId == R.id.navigation_store){
                    loadFragment(new StoreFragment(), false);
                    number_Menu = 0;
                } else if (itemId == R.id.navigation_library){
                    loadFragment(new LibraryFragment(), false);
                    number_Menu = 1;
                } else  if (itemId == R.id.navigation_setting){
                    loadFragment(new SettingFragment(), false);
                    number_Menu = 2;
                }

                return true;
            }
        });
        loadFragment(new StoreFragment(), true);
    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isAppInitialized){
            fragmentTransaction.add(R.id.frameLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);

        }
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
    class MyGesture extends GestureDetector.SimpleOnGestureListener{
        public boolean onFling (MotionEvent e1, MotionEvent e2, float x, float y){
//        kéo từ trái sang phải
            if (e2.getX() - e1.getX() > SWIPE_THRESHOLD && Math.abs(x) > SWIPE_VELOCITY_THRESHOLD){
                if (number_Menu == 0){
                    loadFragment(new StoreFragment(), true);
                } else if(number_Menu == 1){
                    loadFragment(new StoreFragment(), true);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_store).setChecked(true);
                    number_Menu = 0;
                } else {
                    loadFragment(new LibraryFragment(), false);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_library).setChecked(true);
                    number_Menu = 1;
                }
            }
//        kéo từ phải sang trái
            if (e1.getX() - e2.getX() > SWIPE_THRESHOLD && Math.abs((x))> SWIPE_VELOCITY_THRESHOLD){
                if (number_Menu == 0){
                    loadFragment(new LibraryFragment(), false);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_library).setChecked(true);
                    number_Menu = 1;
                } else if(number_Menu == 1){
                    loadFragment(new SettingFragment(), false);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_setting).setChecked(true);
                    number_Menu = 2;
                } else {
                    loadFragment(new SettingFragment(), false);
                }
            }
            return super.onFling(e1, e2, x, y);
        }
    }

}

