package com.example.mobile_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.Commentaire;
import com.example.mobile_project.entity.Post;
import com.example.mobile_project.entity.PostWithCommentaires;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.mobile_project.adapters.PostAdapter;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    ImageView home;

    FloatingActionButton addBtn;
    private RecyclerView recyclerView;
    private List<Post> postList;
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