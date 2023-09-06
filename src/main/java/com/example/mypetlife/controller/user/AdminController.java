package com.example.mypetlife.controller.user;

import com.example.mypetlife.dto.community.article.ArticleResponse;
import com.example.mypetlife.dto.community.article.CreateArticleRequest;
import com.example.mypetlife.dto.community.comment.CreateAndUpdateCommentRequest;
import com.example.mypetlife.entity.community.article.*;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.service.UserService;
import com.example.mypetlife.service.community.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "관리자", description = "관리자 API")
public class AdminController {

    private final ArticleService articleService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;

    /**
     * [POST] /community/notice
     * 관리자: 공지글 작성
     */
    @PostMapping("/community/notice")
    @Operation(summary = "공지 글 등록")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value =
                            "{\"title\":\"공지입니다.\",\"content\":\"기존 공지글 확인하시고 이용부탁드립니다.\",\"category\":\"notice\"}"),
                    schema = @Schema(implementation = CreateArticleRequest.class))
    )
    public ArticleResponse postArticle(HttpServletRequest request,
                                       @RequestPart @Validated CreateArticleRequest dto,
                                       @RequestPart(required = false) List<MultipartFile> imageFiles) {

        // User 조회
        String email = jwtTokenUtils.getEmailFromHeader(request);
        User user = userService.findByEmail(email);

        // Article 생성
        Article article = Article.createArticle(dto.getTitle(), dto.getContent(),
                                                ArticleCategory.NOTICE, user);


        // 이미지 연관관계
        if(imageFiles != null) {
            for (MultipartFile imageFile : imageFiles) {
                article.addImage(ArticleImage.createArticleImage(imageFile.getOriginalFilename()));
            }
        }

        // 게시글 저장
        Long id = articleService.saveArticle(article);

        // 응답 생성
        Article savedArticle = articleService.findById(id);
        ArticleResponse articleResponse = ArticleResponse.createResponse(savedArticle);
        return articleResponse;
    }
}
