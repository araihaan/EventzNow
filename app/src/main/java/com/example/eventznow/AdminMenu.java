package com.example.eventznow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMenu extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        replaceFragment(new AdminHomeFragment());

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnItemSelectedListener(this);


        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new AdminHomeFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.home:
                replaceFragment(new AdminHomeFragment());
                break;
            case R.id.profil:
                replaceFragment(new AdminProfilFragment());
                break;
            case R.id.ticket:
                replaceFragment(new AdminTicketFragment());
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