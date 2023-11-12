package com.example.mobile_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img= findViewById(R.id.imageView2);
        img.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity3.class);
            startActivity(intent);

        });
    }
}