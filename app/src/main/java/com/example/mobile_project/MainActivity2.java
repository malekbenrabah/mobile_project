package com.example.mobile_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobile_project.session.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity3.class);
            startActivity(intent);
            finish();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (sessionManager.getIsAdmin()) {
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation_admin);
        } else {
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation_user);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.person) {
                loadFragment(new ProfileFragment());
                return true;
            } else if (item.getItemId() == R.id.dashboard) {
                if (sessionManager.getIsAdmin()) {
                    Intent adminIntent = new Intent(this, AdminActivity.class);
                    startActivity(adminIntent);
                    return true;
                }
            }
            else if (item.getItemId() == R.id.logout) {
                sessionManager.logout();
                Intent intent = new Intent(this, MainActivity3.class);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(view -> {
            loadFragment(new PostFragment());
        });

        loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }
}
