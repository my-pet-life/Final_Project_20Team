package com.example.mypetlife.controller.community;

import com.example.mypetlife.dto.community.article.ArticleResponse;
import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.article.LikeArticle;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.service.UserService;
import com.example.mypetlife.service.community.ArticleService;
import com.example.mypetlife.service.community.LikeArticleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController {

    private final JwtTokenUtils jwtTokenUtils;
    private final ArticleService articleService;
    private final UserService userService;
    private final LikeArticleService likeArticleService;

    /**
     * [POST] /community/article/{articleId}/like
     * 게시글 좋아요 누르기
     */
    @PostMapping("/community/article/{articleId}/like")
    public ArticleResponse likeArticle(HttpServletRequest request, @PathVariable Long articleId) {

        // 회원 조회
        String email = jwtTokenUtils.getEmailFromHeader(request);
        User user = userService.findByEmail(email);

        // 좋아요 저장/취소
        likeArticleService.saveLike(user, articleId);

        Article article = articleService.findById(articleId);
        return ArticleResponse.createResponse(article);
    }
}
