package com.example.eventznow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemberRegister extends AppCompatActivity {

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("members");
                String username = registerUsername.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                HelperClass helperClass = new HelperClass(username, email, password);
                reference.child(username).setValue(helperClass);
                Toast.makeText(MemberRegister.this, "You have register successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MemberRegister.this, MemberLogin.class);
                startActivity(intent);
                finish();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberRegister.this, MemberLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //Tombol Kembali
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MemberRegister.this, ActivityWelcome.class);
        startActivity(intent);
        finish();
    }
}