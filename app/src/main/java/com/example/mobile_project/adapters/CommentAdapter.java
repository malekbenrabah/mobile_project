package com.example.mobile_project.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project.R;
import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.Commentaire;
import com.example.mobile_project.entity.User;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Commentaire> comments = new ArrayList<>();
    private int postId; // ID de la publication actuelle

    public void setComments(List<Commentaire> comments, int postId) {
        this.comments = comments;
        this.postId = postId;
        notifyDataSetChanged();
    }

    public void addComment(String commentText) {
        Commentaire newComment = new Commentaire();
        comments.add(newComment);
        notifyItemInserted(comments.size() - 1);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Commentaire comment = comments.get(position);
        holder.commentTextView.setText(comment.getText());
        loadUserDetails(comment.getUserId(), holder);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView commentTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.comment_text);
            usernameTextView = itemView.findViewById(R.id.user_name_comment);
        }
    }

    private void loadUserDetails(int userId, CommentViewHolder holder) {
        LiveData<User> userLiveData = AppDatabase.getAppDatabase(holder.itemView.getContext()).userDao().findById(userId);
        userLiveData.observe((LifecycleOwner) holder.itemView.getContext(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    holder.usernameTextView.setText(user.getUserName());
                } else {
                    Log.d("UserDetails", "loadUserDetails: User is null");
                }
            }
        });
    }
}
