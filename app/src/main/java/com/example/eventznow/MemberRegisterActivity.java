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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemberRegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private TextView loginText;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_register);

        // Initialize Firebase Authentication and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Obtain the references to the username, email, password, and register button
        usernameEditText = findViewById(R.id.etUsername);
        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);
        registerButton = findViewById(R.id.btMemberRegister);
        loginText = findViewById(R.id.textLogin);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the input fields
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Validate the input fields
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Register the user with Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Registration successful, get the registered user
                                FirebaseUser user = mAuth.getCurrentUser();

                                if (user != null) {
                                    // Set the user's display name with the username
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(updateProfileTask -> {
                                                if (updateProfileTask.isSuccessful()) {
                                                    // User's display name updated successfully
                                                    // Save the user data to the Firebase Realtime Database
                                                    String userId = user.getUid();
                                                    User newUser = new User(userId, username, email, "member");
                                                    mDatabase.child(userId).setValue(newUser);

                                                    // Registration and data saving completed, navigate to the next screen
                                                    Intent intent = new Intent(MemberRegisterActivity.this, MemberLoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    // Failed to update the user's display name
                                                    Toast.makeText(getApplicationContext(), "Failed to update display name", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                // Registration failed
                                Toast.makeText(getApplicationContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberRegisterActivity.this, MemberLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Tombol Kembali
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MemberRegisterActivity.this, ActivityWelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}

