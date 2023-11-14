package com.example.mobile_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project.adapters.CommentAdapter;
import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.PostWithCommentaires;
import com.example.mobile_project.entity.Commentaire;

import java.util.List;

public class PostDetails extends Fragment {

    private TextView titleTextView;
    private TextView descriptionTextView;
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private EditText editTextComment;
    private Button buttonAddComment;

    private int postId;
    private int userId;
    private SharedPreferences sp;
    private AppDatabase database;

    public PostDetails() {
        // Constructeur par défaut requis par Android
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        database = AppDatabase.getAppDatabase(getActivity().getApplicationContext());
        sp = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

        titleTextView = view.findViewById(R.id.details_title);
        descriptionTextView = view.findViewById(R.id.details_description);
        commentsRecyclerView = view.findViewById(R.id.comments_recycler_view);
        editTextComment = view.findViewById(R.id.editTextComment);
        buttonAddComment = view.findViewById(R.id.buttonAddComment);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        commentsRecyclerView.setLayoutManager(layoutManager);

        commentAdapter = new CommentAdapter();
        commentsRecyclerView.setAdapter(commentAdapter);
     /*   FrameLayout frameHome = requireActivity().findViewById(R.id.frame_home);
        if (frameHome != null) {
            frameHome.setVisibility(View.GONE);
        }*/
        Bundle args = getArguments();

        if (args != null) {
            PostWithCommentaires postWithComments = (PostWithCommentaires) args.getSerializable("post_with_comments");
            if (postWithComments != null) {
                titleTextView.setText(postWithComments.post.getTitle());
                descriptionTextView.setText(postWithComments.post.getDescription());
                commentAdapter.setComments(postWithComments.commentaires, postWithComments.post.getId());
                postId = postWithComments.post.getId();
                userId = sp.getInt("userId", -1);
            }
        }

        buttonAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = editTextComment.getText().toString();
                commentAdapter.addComment(commentText);
                insertCommentIntoDatabase(commentText);
                editTextComment.setText("");
            }
        });

        loadCommentsFromRoom();

        return view;
    }

    private void insertCommentIntoDatabase(String commentText) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Commentaire commentaire = new Commentaire();
                commentaire.setPostId(postId);
                commentaire.setUserId(userId);
                commentaire.setText(commentText);
                database.commentaireDao().insertCommentaire(commentaire);
                Log.d("comments", database.commentaireDao().getAll().toString());
                return null;
            }
        }.execute();
    }

    private void loadCommentsFromRoom() {
        new AsyncTask<Void, Void, LiveData<List<Commentaire>>>() {
            @Override
            protected LiveData<List<Commentaire>> doInBackground(Void... voids) {
                return database.commentaireDao().getCommentsForPost(postId);
            }

            @Override
            protected void onPostExecute(LiveData<List<Commentaire>> commentsLiveData) {
                super.onPostExecute(commentsLiveData);

                if (commentsLiveData != null) {
                    commentsLiveData.observe(getViewLifecycleOwner(), new Observer<List<Commentaire>>() {
                        @Override
                        public void onChanged(List<Commentaire> commentsList) {
                            commentAdapter.setComments(commentsList, postId);
                        }
                    });
                } else {
                    Log.d("Comments", "Comments LiveData is null");
                }
            }
        }.execute();
    }
}
