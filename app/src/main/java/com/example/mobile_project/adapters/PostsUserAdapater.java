package com.example.mobile_project.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.mobile_project.R;
import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.Post;
import com.example.mobile_project.entity.User;
import com.example.mobile_project.entity.UserWithPosts;
import com.example.mobile_project.listeners.OnDeletePostListener;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

public class PostsUserAdapater extends RecyclerView.Adapter<PostsUserAdapater.PostViewHolder>{

    private List<Post> postList;
    AppDatabase database;


    private OnDeletePostListener deletePostListener;

    public PostsUserAdapater(List<Post> postList, OnDeletePostListener deletePostListener) {
        this.postList = postList;
        this.deletePostListener = deletePostListener;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        notifyDataSetChanged();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView postTypeTextView;
        TextView dateTextView;

        TextView locationTextView;

        ImageView postImageView;

        ImageView deleteButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            postTypeTextView = itemView.findViewById(R.id.post_type);
            dateTextView = itemView.findViewById(R.id.post_created_at);
            postImageView = itemView.findViewById(R.id.post_image);
            locationTextView = itemView.findViewById(R.id.post_location);

            //delete
            deleteButton= itemView.findViewById(R.id.post_trash);
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Post post = postList.get(position);
                    // Call a method to delete the post
                    deletePostListener.onDeletePost(post);
                }
            });

        }
    }



    @NonNull
    @Override
    public PostsUserAdapater.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_posts, parent, false);
        return new PostsUserAdapater.PostViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PostsUserAdapater.PostViewHolder holder, int position) {
        if (postList != null && position < postList.size()) {
            Post post = postList.get(position);

            holder.titleTextView.setText(post.getTitle());
            holder.descriptionTextView.setText(post.getDescription());
            holder.dateTextView.setText(post.getCreated_at());
            holder.postTypeTextView.setText(post.getPost_type());
            String location = post.getVille().toString()+" , "+post.getRegion();
            holder.locationTextView.setText(location);

            Glide.with(holder.itemView.getContext())
                    .load(new File(post.getPhoto()))
                    .placeholder(R.drawable.photos) // Placeholder image while loading
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.postImageView);


        }
    }

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }




}
