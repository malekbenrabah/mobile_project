package com.example.mobile_project.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mobile_project.dao.ChatDao;
import com.example.mobile_project.database.AppDatabase;
import com.example.mobile_project.entity.Chat;
import com.example.mobile_project.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository {
    private final ChatDao chatDao;
    private final LiveData<List<Chat>> allChats;
    SessionManager sessionManager;

    public ChatRepository(Application application) {
        AppDatabase database = AppDatabase.getAppDatabase(application);
        chatDao = database.chatDao();
        allChats = chatDao.getChats();
    }

    public LiveData<List<Chat>> getAllChats() {
        return allChats;
    }

    public void insertChat(Chat chat) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            chatDao.insert(chat);
        });
    }
}
