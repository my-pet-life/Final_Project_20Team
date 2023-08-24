package com.example.mypetlife.controller.community;

import com.example.mypetlife.dto.MessageResponse;
import com.example.mypetlife.dto.community.comment.CreateAndUpdateCommentRequest;
import com.example.mypetlife.dto.community.comment.CreateCommentResponse;
import com.example.mypetlife.entity.Comment;
import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.service.UserService;
import com.example.mypetlife.service.community.ArticleService;
import com.example.mypetlife.service.community.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public CreateCommentResponse addComment(@RequestBody CreateAndUpdateCommentRequest dto,
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

    /**
     * [PUT] /community/article/{articleId}/{commentId}
     * 댓글 수정
     */
    @PutMapping("/community/article/{articleId}/{commentId}")
    public CreateCommentResponse updateComment(@RequestBody CreateAndUpdateCommentRequest dto,
                                               @PathVariable Long articleId,
                                               @PathVariable Long commentId,
                                               HttpServletRequest request) {

        // 회원 검증
        String email = jwtTokenUtils.getEmailFromHeader(request);
        User loginUser = userService.findByEmail(email);

        Comment comment = commentService.findById(commentId);
        if(!comment.getUser().equals(loginUser)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 댓글 수정
        commentService.updateComment(comment, dto.getContent());

        return CreateCommentResponse.createResponse(comment, loginUser, comment.getArticle());
    }

    /**
     * [DELETE] /community/article/{articleId}/{commentId}
     * 댓글 삭제
     */
    @DeleteMapping("/community/article/{articleId}/{commentId}")
    public MessageResponse deleteComment(@PathVariable Long articleId,
                                               @PathVariable Long commentId,
                                               HttpServletRequest request) {

        // 회원 검증
        String email = jwtTokenUtils.getEmailFromHeader(request);
        User loginUser = userService.findByEmail(email);

        Comment comment = commentService.findById(commentId);
        if(!comment.getUser().equals(loginUser)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 댓글 삭제
        commentService.deleteComment(comment);

        return new MessageResponse("댓글이 삭제되었습니다");
    }
}
