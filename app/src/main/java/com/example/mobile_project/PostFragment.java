package com.example.mobile_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class PostFragment extends Fragment {

    Button lost, found;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        lost=view.findViewById(R.id.lost);
        lost.setOnClickListener(view1 -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,new LostFragment()).commit();

        });

        found=view.findViewById(R.id.found);
        found.setOnClickListener(view1 -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,new FoundFragment()).commit();

        });
        return view;
    }
}