package com.example.mypetlife.dto.community.article;

import com.example.mypetlife.entity.Comment;
import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.article.ArticleImage;
import com.example.mypetlife.entity.article.CategoryArticle;
import com.example.mypetlife.entity.article.Tag;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleResponse {

    private Long id;
    private String title;
    private String content;
    private CategoryArticle category;
    private LocalDateTime postDate;
    private Integer like;
    private String username;
    private List<CommentDto> commentDtos = new ArrayList<>();
    private List<TagDto> tagDtos = new ArrayList<>();
    private List<ArticleImageDto> imageDtos = new ArrayList<>();

    //==생성 메서드==//
    public static ArticleResponse createResponse(Article article, List<Comment> comments,
                                                 List<Tag> tags, List<ArticleImage> articleImages) {

        ArticleResponse response = new ArticleResponse();
        response.id = article.getId();
        response.title = article.getTitle();
        response.content = article.getContent();
        response.category = article.getCategory();
        response.postDate = article.getPostDate();
        response.like = article.getLikes();
        response.username = article.getUser().getUsername();

        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtos.add(CommentDto.createDto(comment));
        }
        response.commentDtos = commentDtos;

        List<TagDto> tagDtos = new ArrayList<>();
        for (Tag tag : tags) {
            tagDtos.add(TagDto.createDto(tag));
        }
        response.tagDtos = tagDtos;

        List<ArticleImageDto> articleImageDtos = new ArrayList<>();
        for (ArticleImage articleImage : articleImages) {
            articleImageDtos.add(ArticleImageDto.createDto(articleImage));
        }
        response.imageDtos = articleImageDtos;

        return response;
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class CommentDto {

        private String username;
        private String content;
        private String imageUrl;
        private LocalDateTime commentDate;

        public static CommentDto createDto(Comment comment) {

            CommentDto commentDto = new CommentDto();
            commentDto.username = comment.getUser().getUsername();
            commentDto.content = comment.getContent();
            commentDto.imageUrl = comment.getImageUrl();
            commentDto.commentDate = comment.getCommentDate();
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
