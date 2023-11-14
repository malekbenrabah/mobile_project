package com.example.mobile_project.dao;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT * FROM post where id=:id")
    Post getPostById(int id);

    @Query("UPDATE post SET likes = :likes + 1 WHERE id = :postId")
    void updateLikes(int postId, int likes);

    @Query("UPDATE post SET dislikes = :dislikes + 1 WHERE id = :postId")
    void updateDislikes(int postId, int dislikes);
    @Query("SELECT * FROM post ORDER BY likes DESC")
    LiveData<List<Post>> getMostLikedPosts();

}
