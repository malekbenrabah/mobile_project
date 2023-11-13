package com.example.mobile_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class PostFragment extends Fragment {

    Button lost, found;

    private SharedPreferences sp;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        sp = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        lost=view.findViewById(R.id.lost);
        lost.setOnClickListener(view1 -> {
            editor.putString("typePost", "lost");
            editor.commit();

            Intent intent = new Intent(requireActivity(),PostActivity.class);
            startActivity(intent);

        });

        found=view.findViewById(R.id.found);
        found.setOnClickListener(view1 -> {
            editor.putString("typePost", "found");
            editor.commit();

            Intent intent = new Intent(requireActivity(),PostActivity.class);
            startActivity(intent);
        });
        return view;
    }
}