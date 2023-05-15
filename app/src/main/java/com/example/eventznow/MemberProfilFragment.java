package com.example.eventznow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemberProfilFragment extends Fragment {

    private TextView usernameTextView;
    private TextView emailTextView;
    private Button logoutButton, about;

    public MemberProfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_profil, container, false);

        usernameTextView = view.findViewById(R.id.txtMemberUsername);
        emailTextView = view.findViewById(R.id.txtMemberEmail);
        logoutButton = view.findViewById(R.id.btnLogout);
        about = view.findViewById(R.id.btnAbout);

        // Get the currently logged-in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            String username = user.getUsername();
                            String email = user.getEmail();
                            usernameTextView.setText(username);
                            emailTextView.setText(email);
                        } else {
                            showErrorToast("User data is null");
                        }
                    } else {
                        showErrorToast("User data does not exist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showErrorToast("Database error: " + databaseError.getMessage());
                }
            });
        } else {
            showErrorToast("User is null");
        }

        // Initialize the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the logout method
                logout();
            }
        });

        // Initialize the about button
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the about activity
                Intent intent = new Intent(getActivity(), About.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();

        // Redirect the user to the login screen
        Intent intent = new Intent(getActivity(), MemberLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void showErrorToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}

