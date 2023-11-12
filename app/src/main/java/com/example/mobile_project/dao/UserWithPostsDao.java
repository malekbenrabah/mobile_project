package com.example.mobile_project.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mobile_project.entity.UserWithPosts;

import java.util.List;

@Dao
public interface UserWithPostsDao {
    @Transaction
    @Query("SELECT * FROM User")
    public List<UserWithPosts> getUsersWithPosts();
}
