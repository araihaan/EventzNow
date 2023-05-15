package com.example.eventznow;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminProfilFragment extends Fragment {

    public AdminProfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DatabaseReference profile;
        TextView loginUsername, loginEmail;
        Button about, changeprofile;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_profil, container, false);

        loginUsername = view.findViewById(R.id.txtMemberUsername);
        loginEmail = view.findViewById(R.id.txtMemberEmail);
        about = view.findViewById(R.id.btnAbout);
        changeprofile = view.findViewById(R.id.btnChangeprofile);

        profile = FirebaseDatabase.getInstance().getReference().child("admins");
        profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    loginUsername.setText(username);
                    loginEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        return view;
    }
}
