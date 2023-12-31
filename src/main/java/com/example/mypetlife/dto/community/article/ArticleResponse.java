package com.example.mypetlife.dto.community.article;

import com.example.mypetlife.entity.community.comment.Comment;
import com.example.mypetlife.entity.community.article.*;
import com.example.mypetlife.entity.user.PetSpecies;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * 게시글 하나 조회
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleResponse {

    private Long id;
    private String title;
    private String content;
    private ArticleCategory category;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer like;
    private String username;
    private PetSpecies petSpecies;
    private List<CommentDto> comments = new ArrayList<>();
    private List<TagDto> tags = new ArrayList<>();
    private List<ArticleImageDto> images = new ArrayList<>();

    //==생성 메서드==//
    public static ArticleResponse createResponse(Article article) {

        ArticleResponse response = new ArticleResponse();
        response.id = article.getId();
        response.title = article.getTitle();
        response.content = article.getContent();
        response.category = article.getCategory();
        response.createdDate = article.getCreatedDate();
        response.updatedDate = article.getUpdatedDate();
        response.like = article.getLikeArticles().size();
        response.username = article.getUser().getUsername();
        response.petSpecies = article.getUser().getPetSpecies();

        if(!article.getComments().isEmpty()) {
            List<CommentDto> commentDtos = new ArrayList<>();
            List<Comment> comments = article.getComments();
            for (Comment comment : comments) {
                commentDtos.add(CommentDto.createDto(comment));
            }
            response.comments = commentDtos;
        }

        if(!article.getArticleTags().isEmpty()) {
            List<TagDto> tagDtos = new ArrayList<>();
            List<ArticleTag> articleTags = article.getArticleTags();
            for (ArticleTag articleTag : articleTags) {
                Tag tag = articleTag.getTag();
                tagDtos.add(TagDto.createDto(tag));
            }
            response.tags = tagDtos;
        }

        if(!article.getArticleImages().isEmpty()) {
            List<ArticleImageDto> articleImageDtos = new ArrayList<>();
            List<ArticleImage> articleImages = article.getArticleImages();
            for (ArticleImage articleImage : articleImages) {
                articleImageDtos.add(ArticleImageDto.createDto(articleImage));
            }
            response.images = articleImageDtos;
        }

        return response;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class CommentDto {

        private String username;
        private String content;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
        private int likeCount;

        public static CommentDto createDto(Comment comment) {

            CommentDto commentDto = new CommentDto();
            commentDto.username = comment.getUser().getUsername();
            commentDto.content = comment.getContent();
            commentDto.createdDate = comment.getCreatedDate();
            commentDto.updatedDate = comment.getUpdatedDate();
            commentDto.likeCount = comment.getLikeComments().size();
            return commentDto;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class TagDto {

        private String tagName;

        public static TagDto createDto(Tag tag) {

            TagDto tagDto = new TagDto();
            tagDto.tagName = tag.getTagName();
            return tagDto;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class ArticleImageDto {

        private String imageUrl;

        public static ArticleImageDto createDto(ArticleImage articleImage) {

            ArticleImageDto articleImageDto = new ArticleImageDto();
            articleImageDto.imageUrl = articleImage.getImageUrl();
            return articleImageDto;
        }
    }
}
