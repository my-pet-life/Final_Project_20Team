package com.example.mypetlife.entity.community.article;

import com.example.mypetlife.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeArticle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    //==생성 메서드==//
    public static LikeArticle createLikeArticle(User user, Article article) {

        LikeArticle likeArticle = new LikeArticle();
        user.addLike(likeArticle);
        article.addLike(likeArticle);

        return likeArticle;
    }

    //==수정 메서드==//
    public void setArticle(Article article) {
        this.article = article;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
