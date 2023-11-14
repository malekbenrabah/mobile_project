package com.example.mobile_project.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.mobile_project.listeners.UpdatePostListener;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

public class PostsUserAdapater extends RecyclerView.Adapter<PostsUserAdapater.PostViewHolder>{

    private List<Post> postList;
    AppDatabase database;


    private OnDeletePostListener deletePostListener;

    private UpdatePostListener updatePostListener;

    private OnLikeDislikeClickListener likeDislikeClickListener;

    public void setOnLikeDislikeClickListener(OnLikeDislikeClickListener listener) {
        this.likeDislikeClickListener = listener;
    }

    public interface OnLikeDislikeClickListener {
        void onLikeClick(int position);
        void onDislikeClick(int position);
    }

    public PostsUserAdapater(List<Post> postList, OnDeletePostListener deletePostListener , UpdatePostListener updatePostListener) {
        this.postList = postList;
        this.deletePostListener = deletePostListener;
        this.updatePostListener = updatePostListener;

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
        TextView likeCountTextView ;
        TextView dislikeCountTextView;

        TextView locationTextView;

        ImageView postImageView;

        ImageView deleteButton;

        ImageView updateButton;
        Button likeButton;
        Button dislikeButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            postTypeTextView = itemView.findViewById(R.id.post_type);
            dateTextView = itemView.findViewById(R.id.post_created_at);
            postImageView = itemView.findViewById(R.id.post_image);
            locationTextView = itemView.findViewById(R.id.post_location);
            likeButton = itemView.findViewById(R.id.likeButton);
            dislikeButton = itemView.findViewById(R.id.dislikeButton);
            likeCountTextView = itemView.findViewById(R.id.likeCountTextView);
            dislikeCountTextView = itemView.findViewById(R.id.dislikeCountTextView);

            //delete
            deleteButton= itemView.findViewById(R.id.post_trash);
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Post post = postList.get(position);
                    // delete the post
                    deletePostListener.onDeletePost(post);
                }
            });

            // update
            updateButton = itemView.findViewById(R.id.post_update);
            updateButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Post post = postList.get(position);
                    //update the post
                    updatePostListener.onUpdatePost(post);
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
            int likeCount = post.getLikes();
            holder.likeCountTextView.setText("Likes: " + likeCount);
            int dislikeCount = post.getDislikes();
            holder.dislikeCountTextView.setText("Dislikes: " + dislikeCount);


        }
        holder.likeButton.setOnClickListener(v -> {
            if (likeDislikeClickListener != null) {
                likeDislikeClickListener.onLikeClick(position);
            }
        });

        holder.dislikeButton.setOnClickListener(v -> {
            if (likeDislikeClickListener != null) {
                likeDislikeClickListener.onDislikeClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }

    public Post getItem(int position) {
        return postList != null && position < postList.size() ? postList.get(position) : null;
    }
}
