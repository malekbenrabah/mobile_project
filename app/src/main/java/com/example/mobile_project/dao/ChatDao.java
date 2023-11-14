package com.example.mobile_project.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mobile_project.entity.Chat;

import java.util.List;

@Dao
public interface ChatDao {
    @Query("SELECT * FROM chat")
    LiveData<List<Chat>> getChats();

    @Query("SELECT * FROM chat WHERE senderId = :userId or receiverId = :userId")
    LiveData<List<Chat>> getChatsByUserId(int userId);

    @Insert
    void insert(Chat chat);
}
