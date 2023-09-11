package com.example.mypetlife.entity.community.comment;

import com.example.mypetlife.entity.community.BaseEntity;
import com.example.mypetlife.entity.community.article.Article;
import com.example.mypetlife.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id")
    private Article article;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<LikeComment> likeComments = new ArrayList<>();

    //==생성 메서드==//
    public static Comment createComment(String content, User user, Article article) {

        Comment comment = new Comment();
        comment.content = content;
        comment.user = user;
        article.addComment(comment);

        return comment;
    }

    //==수정 메서드==//
    public void setArticle(Article article) {
        this.article = article;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    //==연관관계 편의 메서드==//
    public void addLikeComment(LikeComment likeComment) {

        this.likeComments.add(likeComment);
        likeComment.comment = this;
    }
}
