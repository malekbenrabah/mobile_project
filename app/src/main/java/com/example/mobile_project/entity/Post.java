package com.example.mobile_project.entity;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "post")
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;

    private String created_at;

    private String post_type;
    
    public void setType(PostType type) {
        this.type = type;
    }

    private PostType type;
    private String photo;
    private String ville;
    private String region;
    @ColumnInfo(name = "userId")
    private int userId;

    public Post() {
    }

    @Ignore
    public Post(String title, String description, String created_at, int userId, String photo, String ville, String region, PostType type) {
        this.title = title;
        this.description = description;
        this.created_at = created_at;
        this.userId = userId;
        this.photo = photo;
        this.ville = ville;
        this.region = region;
        this.type = type;
    }

    public PostType getType() {
        return type;
    }

    public String getCurrentDate() {

        Date currentDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

        String formattedDate = sdf.format(currentDate);

        Log.d("FormattedDate", formattedDate);

        return formattedDate;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoto() {
        return photo;
    }


    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", created_at='" + created_at + '\'' +
                ", post_type='" + post_type + '\'' +
                ", type=" + type +
                ", photo='" + photo + '\'' +
                ", ville='" + ville + '\'' +
                ", region='" + region + '\'' +
                "type='" + type + '\'' +
                ", userId=" + userId +
                '}';
    }
}
