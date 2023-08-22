package com.example.mypetlife.entity;

import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @Column(name = "comment_date")
    private LocalDateTime commentDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    //==생성 메서드==//
    public static Comment createComment(String content, User user, Article article) {

        Comment comment = new Comment();
        comment.content = content;
        comment.commentDate = LocalDateTime.now();
        comment.user = user;
        article.addComment(comment);

        return comment;
    }

    //==수정 메서드==//
    public void setArticle(Article article) {
        this.article = article;
    }
}
