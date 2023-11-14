package com.example.mobile_project.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Chat {
    @PrimaryKey(autoGenerate = true)
    private int chatId;
    private int senderId;
    private int receiverId;

    public Chat(int senderId, int receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chatId=" + chatId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return chatId == chat.chatId && senderId == chat.senderId && receiverId == chat.receiverId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, senderId, receiverId);
    }
}
