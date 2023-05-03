package com.example.eventznow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MemberMenu extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_menu);

        replaceFragment(new HomeFragment());

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.home:
                replaceFragment(new HomeFragment());
                break;
            case R.id.profil:
                replaceFragment(new ProfilFragment());
                break;
            case R.id.ticket:
                replaceFragment(new TicketFragment());
                break;
        }

        return true;

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}