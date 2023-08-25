package com.example.mypetlife.dto.community.article;

import com.example.mypetlife.entity.community.article.Article;
import com.example.mypetlife.entity.community.article.ArticleCategory;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleListResponse {

    private Long id;
    private String title;
    private ArticleCategory category;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer like;
    private String username;
    private Integer commentsCount;

    public static ArticleListResponse createResponse(Article article) {

        ArticleListResponse response = new ArticleListResponse();
        response.id = article.getId();
        response.title = article.getTitle();
        response.category = article.getCategory();
        response.createdDate = article.getCreatedDate();
        response.updatedDate = article.getUpdatedDate();
        response.like = article.getLikeArticles().size();
        response.username = article.getUser().getUsername();
        response.commentsCount = article.getComments().size();

        return response;
    }
}
