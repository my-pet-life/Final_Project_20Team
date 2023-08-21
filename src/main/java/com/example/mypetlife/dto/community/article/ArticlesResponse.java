package com.example.mypetlife.dto.community.article;

import com.example.mypetlife.entity.article.CategoryArticle;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ArticlesResponse {

    private final List<ArticleListResponse> articles;
    private final int articlesCount;

    @AllArgsConstructor
    static class ArticleListResponse {
        private final Long id;
        private final String title;
        private final CategoryArticle category;
        private final LocalDateTime postDate;
        private final Integer like;
        private final String username;
        private final Integer commentsCount;
    }
}
