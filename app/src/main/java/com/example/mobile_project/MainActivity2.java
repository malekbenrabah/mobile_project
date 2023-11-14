package com.example.mobile_project;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mobile_project.ChatbotActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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




        Button btnOpenChatbot = findViewById(R.id.btnOpenChatbot);
        btnOpenChatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click, and open the ChatbotActivity
                openChatbotActivity();
            }
        });

        // ... Other setup code ...
    }

    private void openChatbotActivity() {
        Intent intent = new Intent(MainActivity2.this, ChatbotActivity.class);
        startActivity(intent);
    }
}







