package com.example.mypetlife.entity;

import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "comment_date")
    private LocalDateTime commentDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    //==수정 메서드==//
    public void setArticle(Article article) {
        this.article = article;
    }

    //==연관관계 편의 메서드==//
}
