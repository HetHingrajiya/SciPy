package com.example.scipy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        SharedPreferences sp = getSharedPreferences(ConstantSp.pref, MODE_PRIVATE);
        String userId = sp.getString(ConstantSp.userid, "");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!userId.isEmpty()) {
                // User is logged in
                startActivity(new Intent(SplashActivity.this, MainNavigationActivity.class));
            } else {
                // User is not logged in
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();
        }, SPLASH_DELAY);
    }
}
