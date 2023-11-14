package com.example.mobile_project.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Chat.class, parentColumns = "chatId", childColumns = "chatId", onDelete = ForeignKey.CASCADE))
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    private long messageId;
    private int chatId;
    private int senderId;
    private int receiverId;
    private String text;
    private String messageTimestamp;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(String messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public ChatMessage(int chatId, int senderId, int receiverId, String text, String messageTimestamp) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.messageTimestamp = messageTimestamp;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "messageId=" + messageId +
                ", chatId=" + chatId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", text='" + text + '\'' +
                ", messageTimestamp='" + messageTimestamp + '\'' +
                '}';
    }
}
