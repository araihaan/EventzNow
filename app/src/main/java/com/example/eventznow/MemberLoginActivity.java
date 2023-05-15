package com.example.eventznow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MemberLoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_login);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Obtain the references to the username, password, login button, and register text
        usernameEditText = findViewById(R.id.etUsername);
        passwordEditText = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.btMemberLogin);
        registerText = findViewById(R.id.textRegister);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the input fields
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Validate the input fields
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Please enter a email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sign in the user with Firebase Authentication
                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Login successful, navigate to the main activity
                                Intent intent = new Intent(MemberLoginActivity.this, MemberMenuActivity.class);
                                startActivity(intent);
                                finish(); // Optional: Finish the login activity if needed
                            } else {
                                // Login failed
                                Toast.makeText(getApplicationContext(), "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberLoginActivity.this, MemberRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Tombol Kembali
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MemberLoginActivity.this, ActivityWelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}
