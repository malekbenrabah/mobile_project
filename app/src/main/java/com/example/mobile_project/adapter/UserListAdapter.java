package com.example.mobile_project.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile_project.R;
import com.example.mobile_project.dao.UserDao;
import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.User;

import java.io.File;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private List<User> userList;

    public UserListAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Set user information
        String photoPath = user.getPhoto();
        File imageFile = new File(photoPath);
        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            holder.userPhotoImageView.setImageBitmap(bitmap);
        }
        holder.userNameTextView.setText(user.getUserName());
        holder.emailTextView.setText(user.getEmail());
        holder.roleTextView.setText(user.getRole().toString());

        holder.deleteButton.setOnClickListener(v -> {
            // Get the UserDao
            UserDao userDao = AppDatabase.getAppDatabase(v.getContext()).userDao();

            // Start a coroutine to perform the delete operation on a background thread
            AppDatabase.databaseWriteExecutor.execute(() -> {
                userDao.deleteUser(user);
            });

            // Notify the adapter that an item has been removed
            userList.remove(user);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userPhotoImageView;
        TextView userNameTextView;
        TextView emailTextView;
        TextView roleTextView;
        Button deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userPhotoImageView = itemView.findViewById(R.id.userPhotoImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}