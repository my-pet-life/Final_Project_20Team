package com.example.mypetlife.dto.community.comment;

import com.example.mypetlife.entity.Comment;
import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCommentResponse {

    private String content;
    private LocalDateTime commentDate;
    private String username;
    private Long articleId;

    public static CreateCommentResponse createResponse(Comment comment, User user, Article article) {

        CreateCommentResponse response = new CreateCommentResponse();
        response.content = comment.getContent();
        response.commentDate = comment.getCommentDate();
        response.username = user.getUsername();
        response.articleId = article.getId();

        return response;
    }
}
