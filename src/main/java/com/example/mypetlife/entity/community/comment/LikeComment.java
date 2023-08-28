package com.example.mypetlife.entity.community.comment;

import com.example.mypetlife.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LikeComment {

    @Id @GeneratedValue
    @Column(name = "like_comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    //==생성 메서드==//
    public static LikeComment createLikeComment(Comment comment, User user) {

        LikeComment likeComment = new LikeComment();
        comment.addLikeComment(likeComment);
        likeComment.user = user;

        return likeComment;
    }
}
