package com.example.eventznow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeProfileActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etCurrentPassword, etNewPassword;
    private DatabaseReference usersRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);

        // Get the current user's ID
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get the reference to the "users" node in the Realtime Database
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        // Add a ValueEventListener to retrieve the user data
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the username and email from the dataSnapshot
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    // Set the retrieved data into the EditText fields
                    etUsername.setText(username);
                    etEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors here
            }
        });

        // Handle the password change button click
        findViewById(R.id.btSave).setOnClickListener(v -> {
            updateProfile();
            changePassword();
        });

        // Handle the cancel button click
        findViewById(R.id.btCancel).setOnClickListener(v -> cancelAction());
    }

    private void updateProfile() {
        String newUsername = etUsername.getText().toString().trim();
        String newEmail = etEmail.getText().toString().trim();

        // Retrieve the existing username and email from the Realtime Database
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String existingUsername = dataSnapshot.child("username").getValue(String.class);
                    String existingEmail = dataSnapshot.child("email").getValue(String.class);

                    // Check if the new username is a duplicate of the existing username
                    if (newUsername.equals(existingUsername)) {
                        if (!newEmail.equals(existingEmail)) {
                            // Only the email has changed
                            updateEmail(newEmail);
                        }
                        return;
                    }

                    // Check if the new email is a duplicate of the existing email
                    if (newEmail.equals(existingEmail)) {
                        if (!newUsername.equals(existingUsername)) {
                            // Only the username has changed
                            updateUsername(newUsername);
                        }
                        return;
                    }

                    // Both username and email have changed
                    updateUsernameAndEmail(newUsername, newEmail);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }

    private void updateEmail(String newEmail) {
        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Update the email for authentication
            currentUser.updateEmail(newEmail)
                    .addOnCompleteListener(updateEmailTask -> {
                        if (updateEmailTask.isSuccessful()) {
                            // Email update successful, update the email in the Realtime Database as well
                            usersRef.child("email").setValue(newEmail)
                                    .addOnCompleteListener(updateEmailInDatabaseTask -> {
                                        if (updateEmailInDatabaseTask.isSuccessful()) {
                                            Toast.makeText(ChangeProfileActivity.this, "Email Updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ChangeProfileActivity.this, "Failed to Update Email in the Database", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(ChangeProfileActivity.this, "Failed to Update Email", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateUsername(String newUsername) {
        // Update the username in the Realtime Database
        usersRef.child("username").setValue(newUsername)
                .addOnCompleteListener(updateUsernameTask -> {
                    if (updateUsernameTask.isSuccessful()) {
                        Toast.makeText(ChangeProfileActivity.this, "Username Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangeProfileActivity.this, "Failed to Update Username", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUsernameAndEmail(String newUsername, String newEmail) {
        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Update the email for authentication
            currentUser.updateEmail(newEmail)
                    .addOnCompleteListener(updateEmailTask -> {
                        if (updateEmailTask.isSuccessful()) {
                            // Email update successful, update the email in the Realtime Database as well
                            usersRef.child("email").setValue(newEmail)
                                    .addOnCompleteListener(updateEmailInDatabaseTask -> {
                                        if (updateEmailInDatabaseTask.isSuccessful()) {
                                            // Update the username in the Realtime Database
                                            usersRef.child("username").setValue(newUsername)
                                                    .addOnCompleteListener(updateUsernameTask -> {
                                                        if (updateUsernameTask.isSuccessful()) {
                                                            Toast.makeText(ChangeProfileActivity.this, "Username and Email Updated", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(ChangeProfileActivity.this, "Failed to Update Username", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(ChangeProfileActivity.this, "Failed to Update Email in the Database", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(ChangeProfileActivity.this, "Failed to Update Email", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();

        if (newPassword.isEmpty()) {
            // New password field is empty, ignore password change
            return;
        }

        // Check if the current password matches the user's actual password
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, currentPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Current password is correct, proceed with password update
                            currentUser.updatePassword(newPassword)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            Toast.makeText(ChangeProfileActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                            etCurrentPassword.getText().clear();
                                            etNewPassword.getText().clear();
                                        } else {
                                            Toast.makeText(ChangeProfileActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(ChangeProfileActivity.this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void cancelAction() {
        // Get the user's role from the Realtime Database
        DatabaseReference userRoleRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("role");
        userRoleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String role = dataSnapshot.getValue(String.class);
                    if (role != null && role.equals("admin")) {
                        // User is an admin, navigate to AdminMenuActivity
                        startActivity(new Intent(ChangeProfileActivity.this, AdminMenuActivity.class));
                    } else {
                        // User is a member, navigate to MemberMenuActivity
                        startActivity(new Intent(ChangeProfileActivity.this, MemberMenuActivity.class));
                    }
                    // Finish the current activity
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }
}



