package com.example.mobile_project.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class PostWithCommentaires implements Serializable {
    @Embedded
    public Post post;
    @Relation(
            parentColumn = "id",
            entityColumn = "postId"
    )
    public List<Commentaire> commentaires;

    public PostWithCommentaires(Post post, List<Commentaire> comments) {
        this.post=post;
        this.commentaires=comments;
    }


}
