package com.example.mobile_project.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project.R;
import com.example.mobile_project.entity.Post;
import com.example.mobile_project.entity.User;
import com.example.mobile_project.entity.UserWithPosts;

import java.util.List;

/* public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<UserWithPosts> userWithPostsList;

    public PostAdapter(List<UserWithPosts> userWithPostsList) {
        this.userWithPostsList = userWithPostsList;
    }

    public void setUserWithPostsList(List<UserWithPosts> userWithPostsList) {
        this.userWithPostsList = userWithPostsList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView usernameTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        if (userWithPostsList != null && position < userWithPostsList.size()) {
            UserWithPosts userWithPosts = userWithPostsList.get(position);

            User user = userWithPosts.user;
            List<Post> posts = userWithPosts.posts;

            if (posts != null && !posts.isEmpty()) {
                holder.titleTextView.setText(posts.get(0).getTitle());
                holder.descriptionTextView.setText(posts.get(0).getDescription());
                holder.usernameTextView.setText(user.getUserName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return userWithPostsList != null ? userWithPostsList.size() : 0;
    }
}*/
// Mettez à jour PostAdapter.java
// ...

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    // ...
    private List<UserWithPosts> userWithPostsList;

    public PostAdapter(List<UserWithPosts> userWithPostsList) {
        this.userWithPostsList = userWithPostsList;
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView usernameTextView;

        // Ajoutez des vues pour les nouvelles informations (photo de l'utilisateur, image de la publication, type de publication, date de création, etc.)
        ImageView userPhotoImageView;
        ImageView postImageView;
        TextView postTypeTextView;
        TextView postCreatedAtTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            descriptionTextView = itemView.findViewById(R.id.post_description);

            // Initialisez les nouvelles vues ici
            postImageView = itemView.findViewById(R.id.post_image);
            postTypeTextView = itemView.findViewById(R.id.post_type);
            postCreatedAtTextView = itemView.findViewById(R.id.post_created_at);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_posts, parent, false);
        return new PostViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        if (userWithPostsList != null && position < userWithPostsList.size()) {
            UserWithPosts userWithPosts = userWithPostsList.get(position);

            User user = userWithPosts.user;
            List<Post> posts = userWithPosts.posts;

            if (posts != null && !posts.isEmpty()) {
                // Set user details
                holder.usernameTextView.setText(user.getUserName());
                // Set other user details like photo if available

                for(Post post:posts) {
                    // Set post details
                    holder.titleTextView.setText(post.getTitle());
                    holder.descriptionTextView.setText(post.getDescription());
                    // Set other post details

                } // Vous pouvez également définir l'image, le type de post, la date de création, etc.
            }
        }
    }

    @Override
    public int getItemCount() {
        return userWithPostsList != null ? userWithPostsList.size() : 0;
    }
}
