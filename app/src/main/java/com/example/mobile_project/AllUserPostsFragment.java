package com.example.mobile_project;

import static com.example.mobile_project.database.AppDatabase.ioThread;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobile_project.adapters.PostAdapter;
import com.example.mobile_project.adapters.PostsUserAdapater;
import com.example.mobile_project.dao.UserWithPostsDao;
import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.Post;
import com.example.mobile_project.entity.UserWithPosts;

import java.util.ArrayList;
import java.util.List;

import com.example.mobile_project.listeners.OnDeletePostListener;
import com.example.mobile_project.listeners.UpdatePostListener;

public class AllUserPostsFragment extends Fragment  implements OnDeletePostListener, UpdatePostListener {


    AppDatabase database;

    private RecyclerView recyclerView;



    private PostsUserAdapater postAdapter;

    private SharedPreferences sp;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_user_posts, container, false);

        database = AppDatabase.getAppDatabase(getActivity().getApplicationContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postAdapter = new PostsUserAdapater(new ArrayList<>(),this,this);

        recyclerView.setAdapter(postAdapter);


        sp = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        int userId = sp.getInt("userId", -1);


        /*
        ioThread(() -> {
            List<Post> posts = database.postDao().getUserAllPosts(userId);
            for (Post p : posts) {
                System.out.println("post : " + p);
            }

            // Update the adapter with the new data on the UI thread
            requireActivity().runOnUiThread(() -> {
                postAdapter.setPostList(posts);
            });

        });

         */

        loadPostsFromRoom();
        return view;

    }

    private void loadPostsFromRoom() {
        new AsyncTask<Void, Void, List<Post>>() {
            @Override
            protected List<Post> doInBackground(Void... voids) {
                // Récupérez la liste des publications directement depuis la base de données Room
                sp = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                int userId = sp.getInt("userId", -1);


                return AppDatabase.getAppDatabase(requireContext()).postDao().getUserAllPosts(userId);
            }

            @Override
            protected void onPostExecute(List<Post> userWithPostsList) {
                super.onPostExecute(userWithPostsList);

                if (userWithPostsList != null) {
                    for (Post userWithPosts : userWithPostsList) {
                        Log.d("post description", userWithPosts.getDescription());

                    }
                    //postAdapter.notifyDataSetChanged();
                    postAdapter.setPostList(userWithPostsList);

                } else {
                    Log.d("UserWithPosts", "UserWithPosts list is null");
                }
            }

        }.execute();
    }

    @Override
    public void onDeletePost(Post post) {
        ioThread(() -> {
            database.postDao().deletePost(post);
            sp = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
            int userId = sp.getInt("userId", -1);

            List<Post> updatedPosts = database.postDao().getUserAllPosts(userId);

            requireActivity().runOnUiThread(() -> {
                postAdapter.setPostList(updatedPosts);
            });

        });
    }

    @Override
    public void onUpdatePost(Post post) {

        sp = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("postId", post.getId());
        editor.apply();


        UpdatePostFragment updatePostFragment = new UpdatePostFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, updatePostFragment)
                .addToBackStack(null)
                .commit();

    }
}