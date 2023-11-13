package com.example.mobile_project;

import static com.example.mobile_project.database.AppDatabase.ioThread;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.Post;

import java.util.List;


public class MyPostsFragment extends Fragment {

    AppDatabase database;

    private RecyclerView recyclerView;
    private SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_my_posts, container, false);
        database = AppDatabase.getAppDatabase(getActivity().getApplicationContext());

        return view;
    }
}