package com.example.eventznow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class AdminLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
    }
    //Tombol Kembali
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminLogin.this, ActivityWelcome.class);
        startActivity(intent);
        super.onBackPressed(); // call the super method to perform default back action
        // add your own custom logic here if you need to
    }
}