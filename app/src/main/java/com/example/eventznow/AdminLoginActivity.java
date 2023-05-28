package com.example.eventznow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerText;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Initialize Firebase Authentication and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Obtain the references to the username, password, login button, and register text
        usernameEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.btAdminLogin);
        registerText = findViewById(R.id.textRegister);

        loginButton.setOnClickListener(v -> {
            // Get the values from the input fields
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate the input fields
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(getApplicationContext(), "Please enter an email", Toast.LENGTH_SHORT).show();
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
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid();
                                DatabaseReference userRef = mDatabase.child(userId);
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            User userData = snapshot.getValue(User.class);
                                            if (userData != null) {
                                                String role = userData.getRole();
                                                if (role.equals("admin")) {
                                                    // User is a member, navigate to member menu activity
                                                    Intent intent = new Intent(AdminLoginActivity.this, AdminMenuActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else if (role.equals("member")) {
                                                    Toast.makeText(getApplicationContext(), "This account is not admin", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "User data not found", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(), "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            // Login failed
                            Toast.makeText(getApplicationContext(), "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLoginActivity.this, AdminRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Tombol Kembali
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminLoginActivity.this, ActivityWelcomeActivity.class);
        startActivity(intent);
        super.onBackPressed(); // call the super method to perform default back action
        // add your own custom logic here if you need to
    }
}