package com.example.eventznow;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DELAY_MS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent welcomeIntent = new Intent(getApplicationContext(), ActivityWelcomeActivity.class);

        new Handler().postDelayed(() -> {
            startActivity(welcomeIntent);
            finish();
        }, SPLASH_SCREEN_DELAY_MS);
    }
}
