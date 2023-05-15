package com.example.eventznow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }

    // Tombol Kembali
    @Override
    public void onBackPressed() {
        // Get the fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Check if the MemberProfilFragment is in the back stack
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Pop the fragment from the back stack to navigate back
            fragmentManager.popBackStack();
        } else {
            // If the fragment is not in the back stack, finish the activity
            super.onBackPressed();
        }
    }
}