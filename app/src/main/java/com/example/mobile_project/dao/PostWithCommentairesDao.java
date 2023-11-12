package com.example.mobile_project.dao;

import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mobile_project.entity.PostWithCommentaires;

import java.util.List;

public interface PostWithCommentairesDao {
    @Transaction
    @Query("SELECT * FROM POST")
    public List<PostWithCommentaires> getPostWithCommentaires();
}
