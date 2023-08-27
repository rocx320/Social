package com.example.social;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class LaunchFragment extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ActivityFragment activityFragment = new ActivityFragment();
    AlertFragment alertFragment = new AlertFragment();
    MsgFragment msgFragment = new MsgFragment();
    NewPostFragment newPostFragment = new NewPostFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_fragment);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_profile) {
                    // Handle Home selection
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                    return true;
                } else if (itemId == R.id.navigation_activity) {
                    // Handle Dashboard selection
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, activityFragment).commit();
                    return true;
                } else if (itemId == R.id.navigation_create) {
                    // Handle Profile selection
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, newPostFragment).commit();
                    return true;
                } else if (itemId == R.id.navigation_msg) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, msgFragment).commit();
                    return true;
                } else if (itemId == R.id.navigation_notify) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, alertFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }
}