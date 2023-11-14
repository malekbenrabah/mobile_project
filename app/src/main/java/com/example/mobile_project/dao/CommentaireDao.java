package com.example.mobile_project.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mobile_project.entity.Commentaire;
import com.example.mobile_project.entity.User;

import java.util.List;

@Dao
public interface CommentaireDao {

    @Insert
    void insertCommentaire(Commentaire commentaire);

    @Query("SELECT * FROM commentaire")
    List<Commentaire> getAll();
    @Query("SELECT * FROM Commentaire WHERE postId = :postId")
    LiveData<List<Commentaire>> getCommentsForPost(int postId);
}
