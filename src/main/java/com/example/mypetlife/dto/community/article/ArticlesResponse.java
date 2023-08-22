package com.example.mypetlife.dto.community.article;

import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.article.CategoryArticle;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * 게시글 리스트 조회
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticlesResponse {

    private List<ArticleListResponse> articleDtos = new ArrayList<>();
    private int articlesCount;

    public static ArticlesResponse createResponse(List<Article> articles) {

        ArticlesResponse response = new ArticlesResponse();
        for (Article article : articles) {
            response.articleDtos.add(new ArticleListResponse(article.getId(),
                    article.getTitle(), article.getCategory(), article.getPostDate(),
                    article.getLikes(), article.getUser().getUsername(), article.getComments().size()));
        }
        response.articlesCount = articles.size();

        return response;
    }

    @AllArgsConstructor
    @Data
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
