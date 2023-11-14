package com.example.mobile_project.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mobile_project.entity.Post;
import com.example.mobile_project.entity.User;
import com.example.mobile_project.entity.UserWithPosts;

import java.util.List;

@Dao
public interface PostDao {

    @Insert
    void insertPost(Post post);

    @Query("SELECT * FROM post")
    List<Post> getAll();

    @Query("SELECT * FROM post")
    LiveData<List<Post>> getAllLiveData();

    @Query("SELECT * FROM user INNER JOIN post ON user.id = post.userId")
    List<UserWithPosts> getUsersWithPosts();

    @Query("SELECT * FROM post WHERE id = :postId")
    LiveData<Post> findPostById(int postId);





}
