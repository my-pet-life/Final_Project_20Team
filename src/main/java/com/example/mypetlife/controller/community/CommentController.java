package com.example.mypetlife.controller.community;

import com.example.mypetlife.dto.community.comment.CreateCommentRequest;
import com.example.mypetlife.dto.community.comment.CreateCommentResponse;
import com.example.mypetlife.entity.Comment;
import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.service.UserService;
import com.example.mypetlife.service.community.ArticleService;
import com.example.mypetlife.service.community.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    private final ArticleService articleService;

    /**
     * [POST] /community/article/{articleId}/comment
     * 댓글 등록
     */
    @PostMapping("/community/article/{articleId}/comment")
    public CreateCommentResponse addComment(@RequestBody CreateCommentRequest dto,
                           @PathVariable Long articleId,
                           HttpServletRequest request) {

        // 회원
        String email = jwtTokenUtils.getEmailFromHeader(request);
        User user = userService.findByEmail(email);

        // 게시글
        Article article = articleService.findById(articleId);

        // 댓글 생성 및 저장
        Comment comment = Comment.createComment(dto.getContent(), user, article);
        commentService.saveComment(comment);

        return CreateCommentResponse.createResponse(comment, user, article);
    }
}
