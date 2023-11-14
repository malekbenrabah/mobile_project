package com.example.mobile_project.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.mobile_project.entity.Chat;
import com.example.mobile_project.repository.ChatRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private final ChatRepository chatRepository;
    private final LiveData<List<Chat>> chats;

    public ChatViewModel(Application application) {
        super(application);
        chatRepository = new ChatRepository(application);
        chats = chatRepository.getAllChats();
        System.out.println("chats : " + chats);
    }

    public LiveData<List<Chat>> getChats() {
        return chats;
    }
}
