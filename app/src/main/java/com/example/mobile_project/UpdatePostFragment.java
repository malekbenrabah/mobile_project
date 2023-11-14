package com.example.mobile_project;

import static com.example.mobile_project.database.AppDatabase.ioThread;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.mobile_project.adapters.PostsUserAdapater;
import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.Post;
import com.example.mobile_project.listeners.OnDeletePostListener;
import com.example.mobile_project.listeners.UpdatePostListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.mobile_project.listeners.OnDeletePostListener;
import com.example.mobile_project.listeners.UpdatePostListener;


public class UpdatePostFragment extends Fragment implements OnDeletePostListener, UpdatePostListener {


    AppDatabase database;
    private SharedPreferences sp;

    TextView titre,description;
    Spinner spinnerRegion;
    Spinner spinnerVille;
    Spinner spinnerPostType;

    ImageView photoImageView;

    Button updatePost;

    private PostsUserAdapater postAdapter;

    String photoPath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_update_post, container, false);
        database = AppDatabase.getAppDatabase(getActivity().getApplicationContext());

        postAdapter = new PostsUserAdapater(new ArrayList<>(),this,this);

        sp = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        int postId = sp.getInt("postId", -1);


        titre = view.findViewById(R.id.etTitreUpdate);
        description = view.findViewById(R.id.etDescriptionUpdate);
        spinnerPostType = view.findViewById(R.id.type_post);
        spinnerRegion = view.findViewById(R.id.region_spinnerUpdate);
        spinnerVille = view.findViewById(R.id.ville_spinnerUpdate);
        photoImageView = view.findViewById(R.id.photo_ad);
        updatePost = view.findViewById(R.id.btnAddUpdate);

        //intialise les sprinner avec strings
        setSpinnerAdapter(spinnerRegion, R.array.regions_array);
        setSpinnerAdapter(spinnerVille, R.array.tunis_villes_array);
        setSpinnerAdapter(spinnerPostType, R.array.type_post_array);


        //to change the ville when i change the region
        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Update the list of cities based on the selected region
                updateVillesSpinner(spinnerRegion.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        // get post by id
        ioThread(() -> {
            Post post = database.postDao().getPostById(postId);
            photoPath = post.getPhoto();
            if (post != null) {
                //update fragement with post
                requireActivity().runOnUiThread(() -> {
                    titre.setText(post.getTitle());
                    description.setText(post.getDescription());

                    //set selected post's type , ville and region
                    setSpinnerSelection(spinnerRegion, post.getRegion());
                    setSpinnerSelection(spinnerVille, post.getVille());
                    setSpinnerSelection(spinnerPostType, post.getPost_type());



                    // display photo
                    Glide.with(requireContext())
                            .load(new File(post.getPhoto()))
                            .placeholder(R.drawable.photos) // Placeholder image while loading
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(photoImageView);
                });
            }
        });

        updatePost.setOnClickListener(view1 -> {
            String updatedTitle = titre.getText().toString();
            String updatedDescription = description.getText().toString();
            String updatedRegion = spinnerRegion.getSelectedItem().toString();
            String updatedVille = spinnerVille.getSelectedItem().toString();
            String updatedPostType = spinnerPostType.getSelectedItem().toString();
            int userId = sp.getInt("userId", -1);

            Post updatedPost = new Post();
            updatedPost.setId(postId);
            updatedPost.setTitle(updatedTitle);
            updatedPost.setDescription(updatedDescription);
            updatedPost.setRegion(updatedRegion);
            updatedPost.setVille(updatedVille);
            updatedPost.setPost_type(updatedPostType);
            updatedPost.setUserId(userId);
            updatedPost.setPhoto(photoPath);


            ioThread(() -> {
                database.postDao().updatePost(updatedPost);
                requireActivity().runOnUiThread(() -> {
                    navigateToAllUserPostsFragment();
                });


                sp = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

                List<Post> updatedPosts = database.postDao().getUserAllPosts(userId);


                requireActivity().runOnUiThread(() -> {
                    postAdapter.setPostList(updatedPosts);
                });


                List<Post> posts = database.postDao().getAll();
                for (Post p : posts) {
                    System.out.println("post info : " + p);
                }
            });
        });


        return view;
    }

    private void setSpinnerAdapter(Spinner spinner, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), arrayResourceId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(value);
            if (position != -1) {
                spinner.setSelection(position);
            }
        }
    }

    private void updateVillesSpinner(String selectedRegion) {
        int arrayResourceId;
        if ("Tunis".equals(selectedRegion)) {
            arrayResourceId = R.array.tunis_villes_array;
        } else if ("Sousse".equals(selectedRegion)) {
            arrayResourceId = R.array.sousse_villes_array;
        } else {
            // Default to an empty array or handle other regions as needed
            arrayResourceId = R.array.empty_array;
        }

        // Set the adapter for the ville spinner based on the selected region
        setSpinnerAdapter(spinnerVille, arrayResourceId);
    }

    private void navigateToAllUserPostsFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, new AllUserPostsFragment())
                .commit();
    }

    @Override
    public void onDeletePost(Post post) {

    }

    @Override
    public void onUpdatePost(Post post) {

    }
}