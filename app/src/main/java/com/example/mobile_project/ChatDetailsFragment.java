package com.example.mobile_project;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_project.adapter.ChatMessageAdapter;
import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.ChatMessage;
import com.example.mobile_project.entity.User;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ChatMessage> chatMessages;
    private List<User> users;
    AppDatabase db;
    private ChatMessageAdapter chatMessageAdapter;

    public ChatDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = AppDatabase.getAppDatabase(requireContext());

        // Initialize the chat messages (You should replace this with your actual data)
        chatMessages = getChatMessages();
        users = db.userDao().findAllUsers();

        // Create a ChatMessageAdapter and pass the chat messages
        chatMessageAdapter = new ChatMessageAdapter(chatMessages, users);

        // Set the RecyclerView adapter
        recyclerView.setAdapter(chatMessageAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_details, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.chatMessageRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }

    private List<ChatMessage> getChatMessages() {
        // Replace this with your logic to fetch chat messages from your data source
        List<ChatMessage> chatMessages = new ArrayList<>();

        // Sample chat messages
        chatMessages.add(new ChatMessage(1, 1 , 2, "Hello", "12:34 PM"));
        chatMessages.add(new ChatMessage(2, 2, 1, "Hi there!", "12:35 PM"));

        // Add more chat messages as needed

        return chatMessages;
    }
}
