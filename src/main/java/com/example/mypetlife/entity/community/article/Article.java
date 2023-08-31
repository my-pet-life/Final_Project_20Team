package com.example.mypetlife.entity.community.article;

import com.example.mypetlife.entity.community.BaseEntity;
import com.example.mypetlife.entity.community.comment.Comment;
import com.example.mypetlife.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "article_id")
    private Long id;

    private String title;

    private String content;

    @Enumerated(value = EnumType.STRING)
    private ArticleCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "article")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleTag> articleTags = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleImage> articleImages = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<LikeArticle> likeArticles = new ArrayList<>();

    // 좋아요 갯수를 의미하는 가상 칼럼
    @Formula("(select count(*) from like_article where like_article.article_id = article_id)")
    private int likeCount;

    // 댓글 갯수를 의미하는 가상 칼럼
    @Formula("(select count(*) from comment where comment.article_id = article_id)")
    private int commentCount;

    //==생성 메서드==//
    public static Article createArticle(String title, String content, ArticleCategory category, User user) {

        Article article = new Article();
        article.title = title;
        article.content = content;
        article.category = category;
        article.setUser(user);

        return article;
    }

    //==연관관계 편의 메서드==//
    public void addArticleTag(ArticleTag articleTag) {

        articleTag.setArticle(this);
        this.articleTags.add(articleTag);
    }

    public void addImage(ArticleImage articleImage) {

        articleImage.setArticle(this);
        this.articleImages.add(articleImage);
    }

    public void addComment(Comment comment) {

        comment.setArticle(this);
        this.comments.add(comment);
    }

    public void setUser(User user) {

        user.getArticles().add(this);
        this.user = user;
    }

    public void addLike(LikeArticle likeArticle) {

        this.likeArticles.add(likeArticle);
        likeArticle.setArticle(this);
    }

    //==수정 메서드==//
    public void setTitle(String title) {

        this.title = title;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public void setCategory(ArticleCategory category) {

        this.category = category;
    }
}
