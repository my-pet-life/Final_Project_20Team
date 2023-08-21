package com.example.mypetlife.entity.article;

import com.example.mypetlife.entity.Comment;
import com.example.mypetlife.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id @GeneratedValue
    @Column(name = "article_id")
    private Long id;
    private String title;
    private String content;
    @Enumerated(value = EnumType.STRING)
    private CategoryArticle category;

    @Column(name = "post_date")
    private LocalDateTime postDate;
    private Integer likes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleTag> articleTags = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleImage> images = new ArrayList<>();

    //==연관관계 편의 메서드==//
    public void addArticleTag(ArticleTag articleTag) {

        articleTag.setArticle(this);
        this.articleTags.add(articleTag);
    }

    public void addImage(ArticleImage articleImage) {

        articleImage.setArticle(this);
        this.images.add(articleImage);
    }

    public void addComment(Comment comment) {

        comment.setArticle(this);
        this.comments.add(comment);
    }

    public void setUser(User user) {

        user.getArticles().add(this);
        this.user = user;
    }

    public void updateArticleTags(List<ArticleTag> articleTags) {

        this.articleTags = articleTags;
    }

    public void setImages(List<ArticleImage> images) {
        this.images = images;
    }

    //==생성 메서드==//
    public static Article createArticle(String title, String content, CategoryArticle category,
                                        User user, List<ArticleTag> articleTags, List<ArticleImage> articleImages) {

        Article article = new Article();
        article.title = title;
        article.content = content;
        article.category = category;
        article.postDate = LocalDateTime.now();
        article.likes = 0;
        article.setUser(user);

        for (ArticleTag articleTag : articleTags) {
            article.addArticleTag(articleTag);
        }

        for (ArticleImage articleImage : articleImages) {
            article.addImage(articleImage);
        }

        return article;
    }

    //==비즈니스 메서드==//
    public void updateTitle(String title) {

        this.title = title;
    }

    public void updateContent(String content) {

        this.content = content;
    }
}
