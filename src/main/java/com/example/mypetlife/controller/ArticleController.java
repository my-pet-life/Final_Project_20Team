package com.example.mypetlife.controller;

import com.example.mypetlife.dto.community.article.ArticlesResponse;
import com.example.mypetlife.dto.community.article.CreateArticleRequest;
import com.example.mypetlife.dto.community.article.ArticleResponse;
import com.example.mypetlife.dto.community.article.CreateArticleTagDto;
import com.example.mypetlife.entity.Comment;
import com.example.mypetlife.entity.User;
import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.article.ArticleImage;
import com.example.mypetlife.entity.article.CategoryArticle;
import com.example.mypetlife.entity.article.Tag;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.service.ArticleService;
import com.example.mypetlife.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;

    /**
     * [POST] /community
     * 게시글 등록
     */
    @PostMapping("/community")
    public ArticleResponse postArticle(HttpServletRequest request,
                                       @RequestPart @Validated CreateArticleRequest dto,
                                       @RequestPart List<CreateArticleTagDto> tagParam,
                                       @RequestPart(required = false) List<MultipartFile> imageFiles) {

        // 글 작성한 회원 조회
        String email = jwtTokenUtils.getEmailFromHeader(request); // request에서 토큰을 받아서, 토큰에서 email을 받아옴
        User user = userService.findByEmail(email);

        // 태그 리스트 생성
        List<Tag> tags = new ArrayList<>();
        for (CreateArticleTagDto createArticleTagDto : tagParam) {
            tags.add(Tag.createTag(createArticleTagDto.getTagName()));
        }

        // 게시글 이미지 리스트 생성
        List<ArticleImage> articleImages = new ArrayList<>();
        if(!imageFiles.isEmpty()) {
            for (MultipartFile imageFile : imageFiles) {
                articleImages.add(ArticleImage.createArticleImage(imageFile.getOriginalFilename()));
            }
        }

        // 게시글 생성
        Article article = Article.createArticle(dto.getTitle(), dto.getContent(),
                CategoryArticle.valueOf(dto.getCategory()),
                user, tags, articleImages);

        // 게시글 저장
        Long id = articleService.saveArticle(article);

        // 응답 생성
        Article savedArticle = articleService.findById(id);
        ArticleResponse articleResponse = ArticleResponse.createResponse(
                savedArticle, savedArticle.getComments(), savedArticle.getTags(), savedArticle.getImages());
        return articleResponse;
    }

    /**
     * [GET] /community
     * 전체 게시글 조회
     */
    @GetMapping("/community")
    public ArticlesResponse readArticles() {

        List<Article> articles = articleService.findAll();
        ArticlesResponse response = ArticlesResponse.createResponse(articles);
        return response;
    }
}
