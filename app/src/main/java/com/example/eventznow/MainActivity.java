package com.example.eventznow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
        public void run() {
            Intent i = new Intent(getApplicationContext(), ActivityWelcome.class);
            startActivity(i);

            // Menutup activity splash screen
            finish();
        }
    }, 3000);
    }
}