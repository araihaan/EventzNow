package com.example.eventznow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MemberRegisterActivity extends AppCompatActivity {

    EditText registerUsername, registerEmail, registerPassword;
    TextView loginRedirectText;
    Button registerButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_register);

        registerUsername = findViewById(R.id.etUsername);
        registerEmail = findViewById(R.id.etEmail);
        registerPassword = findViewById(R.id.etPassword);
        registerButton = findViewById(R.id.btMemberRegister);
        TextView textView = findViewById(R.id.textLogin);

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance();

        // Set OnClickListener for Register Button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get values from input fields
                String username = registerUsername.getText().toString().trim();
                String email = registerEmail.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();

                // Check if all fields are filled
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MemberRegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check for duplicate username
                reference = database.getReference("members").child(username);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(MemberRegisterActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
                        } else {
                            // Check for duplicate email
                            reference = database.getReference("members");
                            Query query = reference.orderByChild("email").equalTo(email);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Toast.makeText(MemberRegisterActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Register user
                                        HelperClass helperClass = new HelperClass(username, email, password);
                                        reference = database.getReference("members").child(username);
                                        reference.setValue(helperClass);
                                        Toast.makeText(MemberRegisterActivity.this, "You have registered successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MemberRegisterActivity.this, MemberLoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(MemberRegisterActivity.this, "Registration failed, please try again later", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MemberRegisterActivity.this, "Registration failed, please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Set OnClickListener for Login TextView
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberRegisterActivity.this, MemberLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //Tombol Kembali
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MemberRegisterActivity.this, ActivityWelcomeActivity.class);
        startActivity(intent);
        finish();
    }
}