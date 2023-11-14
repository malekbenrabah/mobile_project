package com.example.mobile_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile_project.R;
import com.example.mobile_project.adapter.UserListAdapter;
import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.User;
import java.util.List;

public class UserListFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    private AppDatabase db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        db = AppDatabase.getAppDatabase(requireContext());

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.userListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Use a background thread to fetch users from the database
        loadUsersInBackground();

        return view;
    }

    private void loadUsersInBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform database query on a background thread
                List<User> userList = db.userDao().findAllUsers();

                // Update the UI on the main thread
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Initialize and set the adapter with the retrieved data
                        userListAdapter = new UserListAdapter(userList);
                        recyclerView.setAdapter(userListAdapter);
                    }
                });
            }
        }).start();
    }
}