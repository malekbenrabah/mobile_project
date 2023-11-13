package com.example.mobile_project.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobile_project.entity.Post;
import com.example.mobile_project.entity.User;

import java.util.List;

@Dao
public interface PostDao {

    @Insert
    void insertPost(Post post);

    @Query("SELECT * FROM post")
    List<Post> getAll();

    @Query("SELECT * from post where userId=:userId")
    List<Post> getUserAllPosts(int userId);

    @Delete
    void deletePost(Post post);

    @Update
    void updatePost(Post post);


}
