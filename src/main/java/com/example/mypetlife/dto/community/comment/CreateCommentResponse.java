package com.example.mypetlife.dto.community.comment;

import com.example.mypetlife.entity.community.comment.Comment;
import com.example.mypetlife.entity.community.article.Article;
import com.example.mypetlife.entity.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCommentResponse {

    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String username;
    private Long articleId;

    public static CreateCommentResponse createResponse(Comment comment, User user, Article article) {

        CreateCommentResponse response = new CreateCommentResponse();
        response.content = comment.getContent();
        response.createdDate = comment.getCreatedDate();
        response.updatedDate = comment.getUpdatedDate();
        response.username = user.getUsername();
        response.articleId = article.getId();

        return response;
    }
}
