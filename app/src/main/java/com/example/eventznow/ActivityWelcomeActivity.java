package com.example.eventznow;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityWelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button memberLoginButton = findViewById(R.id.btMemberLogin);
        Button adminLoginButton = findViewById(R.id.btAdminLogin);

        memberLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityWelcomeActivity.this, MemberLoginActivity.class);
            startActivity(intent);
            finish();
        });

        adminLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityWelcomeActivity.this, AdminLoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
