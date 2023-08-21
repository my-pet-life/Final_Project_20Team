package com.example.mypetlife.dto.community.article;

import com.example.mypetlife.entity.Comment;
import com.example.mypetlife.entity.User;
import com.example.mypetlife.entity.article.ArticleImage;
import com.example.mypetlife.entity.article.CategoryArticle;
import com.example.mypetlife.entity.article.Tag;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CreateArticleResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final CategoryArticle category;
    private final LocalDateTime postDate;
    private final Integer like;
    private final String username;
    private final List<CommentDto> commentDtos = new ArrayList<>();
    private final List<TagDto> tagDtos = new ArrayList<>();
    private final List<ArticleImageDto> imageDtos = new ArrayList<>();

    @AllArgsConstructor
    static class CommentDto {

        private String username;
        private String content;
        private String imageUrl;
        private LocalDateTime commentDate;
    }

    @AllArgsConstructor
    static class TagDto {

        private String tagName;
    }

    @AllArgsConstructor
    static class ArticleImageDto {

        private String imageUrl;
    }
}
