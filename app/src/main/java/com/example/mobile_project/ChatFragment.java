package com.example.mobile_project;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mobile_project.adapter.MyChatRecyclerViewAdapter;
import com.example.mobile_project.entity.Chat;
import com.example.mobile_project.repository.ChatRepository;
import java.util.List;

public class ChatFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    public ChatFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        Context context = view.getContext();

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        MyChatRecyclerViewAdapter chatAdapter = new MyChatRecyclerViewAdapter();
        recyclerView.setAdapter(chatAdapter);

        // Retrieve chat data as LiveData and observe it
        ChatRepository chatRepository = new ChatRepository(requireActivity().getApplication());
        LiveData<List<Chat>> chatListLiveData = chatRepository.getAllChats();
        //display the list of chats in the console
        chatListLiveData.observe(getViewLifecycleOwner(), System.out::println);

        // Convert LiveData to a List and populate the adapter
        chatListLiveData.observe(getViewLifecycleOwner(), chatAdapter::submitList);

        return view;
    }
}
