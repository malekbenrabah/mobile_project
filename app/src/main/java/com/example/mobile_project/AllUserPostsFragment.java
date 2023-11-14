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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

    EditText searchEditText;
    ImageView search;
    Button searchButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_user_posts, container, false);

        database = AppDatabase.getAppDatabase(getActivity().getApplicationContext());

        search = view.findViewById(R.id.post_trash);
        searchEditText = view.findViewById(R.id.searchEt);
        searchButton =view.findViewById(R.id.searchButton);
        search.setOnClickListener(v -> {
            searchEditText.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.VISIBLE);

            searchButton.setOnClickListener(view1 -> {
                String searchText = searchEditText.getText().toString();
                filterPosts(searchText);
            });

        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postAdapter = new PostsUserAdapater(new ArrayList<>(),this,this);

        recyclerView.setAdapter(postAdapter);

        postAdapter.setOnLikeDislikeClickListener(new PostsUserAdapater.OnLikeDislikeClickListener() {
            @Override
            public void onLikeClick(int position) {
                Post post = postAdapter.getItem(position);
                int currentLikes = post.getLikes();
                post.setLikes(currentLikes + 1);


                postAdapter.notifyItemChanged(position);
            }

            @Override
            public void onDislikeClick(int position) {
                Post post = postAdapter.getItem(position);
                int currentDislikes = post.getDislikes();
                post.setDislikes(currentDislikes + 1);


                postAdapter.notifyItemChanged(position);
            }
        });

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

    private void filterPosts(String searchText) {
        ioThread(() -> {
            //List<Post> filteredPosts = database.postDao().searchPostsByTitle(searchText);
            List<Post> filteredPosts = database.postDao().search(searchText);

            requireActivity().runOnUiThread(() -> {
                postAdapter.setPostList(filteredPosts);
            });
        });
    }
}