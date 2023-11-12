package com.example.mobile_project.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mobile_project.entity.Post;
import com.example.mobile_project.entity.User;

import java.util.List;

@Dao
public interface PostDao {

    @Insert
    void insertPost(Post post);

    @Query("SELECT * FROM post")
    List<User> getAll();


}
