package com.example.mobile_project.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithCommentaires {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "userId",
            entityColumn = "userId"
    )
    public List<Commentaire> commentaires;
}
