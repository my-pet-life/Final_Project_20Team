package com.example.mypetlife.dto.community.comment;

import com.example.mypetlife.entity.community.comment.Comment;
import com.example.mypetlife.entity.community.article.Article;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponse {

    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String username;
    private Long articleId;
    private int likeCount;

    public static CommentResponse createResponse(Comment comment, Article article) {

        CommentResponse response = new CommentResponse();
        response.content = comment.getContent();
        response.createdDate = comment.getCreatedDate();
        response.updatedDate = comment.getUpdatedDate();
        response.username = comment.getUser().getUsername();
        response.articleId = article.getId();
        response.likeCount = comment.getLikeComments().size();

        return response;
    }
}
