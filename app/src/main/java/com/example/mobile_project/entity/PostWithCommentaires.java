package com.example.mobile_project.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PostWithCommentaires {
    @Embedded
    public Post post;
    @Relation(
            parentColumn = "postId",
            entityColumn = "postId"
    )
    public List<Commentaire> commentaires;
}
