package com.example.mobile_project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (item.getItemId()== R.id.posts) {
                loadFragment(new AllUserPostsFragment());
                return true;
            }
            return false;
        });

        floatingActionButton = findViewById(R.id.faba);
        floatingActionButton.setOnClickListener(view -> {
            loadFragment(new PostFragment());
        });

        loadFragment(new HomeFragment());


        //get fragement
        if (getIntent().hasExtra("fragmentToLoad")) {
            String fragmentToLoad = getIntent().getStringExtra("fragmentToLoad");
            if (fragmentToLoad.equals("PostFragment")) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, new PostFragment())
                        .commit();
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }
}