package com.example.mobile_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project.R;
import com.example.mobile_project.entity.ChatMessage;
import com.example.mobile_project.entity.User;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {
    private List<ChatMessage> chatMessages;
    private List<User> users; // List of users with names

    public ChatMessageAdapter(List<ChatMessage> chatMessages, List<User> users) {
        this.chatMessages = chatMessages;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);

        String senderName = getUserNameById(chatMessage.getSenderId());
        String receiverName = getUserNameById(chatMessage.getReceiverId());

        holder.messageSender.setText("Sender: " + senderName);
        holder.messageText.setText(chatMessage.getText());
        holder.messageTimestamp.setText(chatMessage.getMessageTimestamp());
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageSender;
        TextView messageText;
        TextView messageTimestamp;

        public ViewHolder(View view) {
            super(view);
            messageSender = view.findViewById(R.id.messageSender);
            messageText = view.findViewById(R.id.messageText);
            messageTimestamp = view.findViewById(R.id.messageTimestamp);
        }
    }

    private String getUserNameById(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user.getUserName();
            }
        }
        return "Unknown User";
    }
}
