package com.example.mobile_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity2 extends AppCompatActivity {

    ImageView home;

    FloatingActionButton addBtn;

    Button lost, found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HomeFragment()).commit();
        home= findViewById(R.id.imageViewWelcome);
        home.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HomeFragment()).commit();
        });

        addBtn= findViewById(R.id.fab);
        addBtn.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,new PostFragment()).commit();
        });






    }
}