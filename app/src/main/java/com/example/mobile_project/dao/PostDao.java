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

    @Query("SELECT * FROM post where id=:id")
    Post getPostById(int id);

    @Query("SELECT * FROM post where title=:titre")
    List<Post>searchPostsByTitle(String titre);

    @Query("SELECT * FROM post WHERE title LIKE '%' || :searchText || '%' OR ville LIKE '%' || :searchText || '%' OR region LIKE '%' || :searchText || '%'")
    List<Post> search(String searchText);


}
