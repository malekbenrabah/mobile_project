// PostAdapter.java
package com.example.mobile_project.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project.R;
import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.Post;
import com.example.mobile_project.entity.User;
import com.example.mobile_project.entity.UserWithPosts;

import java.util.List;


/* public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
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
            usernameTextView = itemView.findViewById(R.id.user_name);

            // Initialisez les nouvelles vues ici
            userPhotoImageView = itemView.findViewById(R.id.user_photo);
            postImageView = itemView.findViewById(R.id.post_image);
            postTypeTextView = itemView.findViewById(R.id.post_type);
            postCreatedAtTextView = itemView.findViewById(R.id.post_created_at);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }


    // Mettez à jour onBindViewHolder pour définir les nouvelles informations
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

                // Créez une chaîne pour contenir tous les titres et descriptions des posts
                StringBuilder allPostDetails = new StringBuilder();

                for (Post post : posts) {
                    // Ajoutez les détails du post à la chaîne
                    allPostDetails.append("Title: ").append(post.getTitle()).append("\n");
                    allPostDetails.append("Description: ").append(post.getDescription()).append("\n\n");
                }

                // Affichez tous les détails des posts dans les TextView appropriés
                holder.titleTextView.setText(allPostDetails.toString());
            }
        }
    }


    @Override
    public int getItemCount() {
        return userWithPostsList != null ? userWithPostsList.size() : 0;
    }
} */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postList;
    private OnDetailsButtonClickListener onDetailsButtonClickListener;

    public void setOnDetailsButtonClickListener(OnDetailsButtonClickListener listener) {
        this.onDetailsButtonClickListener = listener;
    }

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    public interface OnDetailsButtonClickListener {
        void onDetailsButtonClick(Post post);
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView usernameTextView;

        ImageView userPhotoImageView;
        ImageView postImageView;
        TextView postTypeTextView;
        TextView postCreatedAtTextView;
        Button detailsButton;




        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            usernameTextView = itemView.findViewById(R.id.user_name);
            userPhotoImageView = itemView.findViewById(R.id.user_photo);
            postImageView = itemView.findViewById(R.id.post_image);
            postTypeTextView = itemView.findViewById(R.id.post_type);
            postCreatedAtTextView = itemView.findViewById(R.id.post_created_at);
            detailsButton = itemView.findViewById(R.id.details_button);
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
        if (postList != null && position < postList.size()) {
            Post post = postList.get(position);

            // Vous devrez peut-être charger les détails de l'utilisateur à partir de l'ID d'utilisateur
            loadUserDetails(post.getUserId(), holder);

            // Set post details
            holder.titleTextView.setText(post.getTitle());
            holder.descriptionTextView.setText(post.getDescription());
            // Set other post details
            holder.detailsButton.setOnClickListener(view -> {
                // Récupérez le post correspondant à la position
                Post selectedPost = postList.get(position);

                // Vérifiez si le gestionnaire de clic est défini
                if (onDetailsButtonClickListener != null) {
                    // Appelez le gestionnaire de clic avec le post sélectionné
                    onDetailsButtonClickListener.onDetailsButtonClick(selectedPost);
                }
            });
        }
    }


    private void loadUserDetails(int userId, PostViewHolder holder) {
        // Chargez les détails de l'utilisateur à partir de l'ID d'utilisateur en utilisant LiveData
        LiveData<User> userLiveData = AppDatabase.getAppDatabase(holder.itemView.getContext()).userDao().findById(userId);

        // Observer pour les changements dans les détails de l'utilisateur
        userLiveData.observe((LifecycleOwner) holder.itemView.getContext(), user -> {
            if (user != null) {
                holder.usernameTextView.setText(user.getUserName());
                // Set other user details like photo if available
            } else {
                Log.d("UserDetails", "loadUserDetails: User is null");
            }
        });
    }


    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }
}

