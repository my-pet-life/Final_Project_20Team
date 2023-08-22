package com.example.mypetlife.controller;

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

import java.util.List;
import java.util.stream.Collectors;

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
    public void likeArticle(HttpServletRequest request, @PathVariable Long articleId) {

        // 회원
        String email = jwtTokenUtils.getEmailFromHeader(request);
        User user = userService.findByEmail(email);

        // 게시글
        Article article = articleService.findById(articleId);

        // 저장
        likeArticleService.saveLike(user, article);
    }
}
