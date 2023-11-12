package com.example.mobile_project.dao;

import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mobile_project.entity.UserWithCommentaires;

import java.util.List;

public interface UserWithCommentairesDao {
    @Transaction
    @Query("SELECT * FROM User")
    public List<UserWithCommentaires> getUsersWithCommentaires();
}
