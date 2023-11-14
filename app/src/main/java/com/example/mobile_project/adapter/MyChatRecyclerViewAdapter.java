package com.example.mobile_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile_project.entity.Chat;
import com.example.mobile_project.R;

public class MyChatRecyclerViewAdapter extends ListAdapter<Chat, MyChatRecyclerViewAdapter.ChatViewHolder> {

    public MyChatRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chats, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        Chat chat = getItem(position);

        // Bind your chat data to the ViewHolder here
        holder.bind(chat);
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        // Define your ViewHolder's views here

        ChatViewHolder(View view) {
            super(view);
            // Initialize your views here
        }

        void bind(Chat chat) {
            // Bind chat data to your views here
        }
    }

    private static final DiffUtil.ItemCallback<Chat> DIFF_CALLBACK = new DiffUtil.ItemCallback<Chat>() {
        @Override
        public boolean areItemsTheSame(Chat oldItem, Chat newItem) {
            return oldItem.getChatId() == newItem.getChatId();
        }

        @Override
        public boolean areContentsTheSame(Chat oldItem, Chat newItem) {
            // Customize this method based on your chat comparison logic
            return oldItem.equals(newItem);
        }
    };
}

