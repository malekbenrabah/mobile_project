package com.example.mobile_project.entity;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "commentaire")
public class Commentaire {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String text;

    private String created_at;

    @ColumnInfo(name = "postId")
    private int postId;

    @ColumnInfo(name = "userId")
    private int userId;

    public Commentaire() {
    }

    @Ignore
    public Commentaire(String text, String created_at, int postId, int userId) {
        this.text = text;
        this.created_at = getCurrentDate();
        this.postId = postId;
        this.userId = userId;
    }

    public String getCurrentDate(){

        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

        String formattedDate = sdf.format(currentDate);

        Log.d("FormattedDate", formattedDate);

        return formattedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", created_at='" + created_at + '\'' +
                ", postId=" + postId +
                ", userId=" + userId +
                '}';
    }
}
