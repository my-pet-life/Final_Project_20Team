package com.example.mypetlife.controller;

import com.example.mypetlife.dto.community.article.CreateArticleRequest;
import com.example.mypetlife.dto.community.article.CreateArticleResponse;
import com.example.mypetlife.entity.user.User;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
    public CreateArticleResponse postArticle(HttpServletRequest request,
                              @RequestPart @Validated CreateArticleRequest dto,
                              @RequestPart(required = false) List<MultipartFile> imageFiles) {

        log.info("컨트롤러실행");
        // 글 작성한 회원 조회
        String email = jwtTokenUtils.getEmailFromHeader(request); // request에서 토큰을 받아서, 토큰에서 email을 받아옴
        User user = userService.findByEmail(email);

        // 태그 리스트 생성
        List<String> tagNames = dto.getTags();
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = Tag.createTag(tagName);
            tags.add(tag);
        }

        // 게시글 이미지 리스트 생성
        List<ArticleImage> articleImages = new ArrayList<>();
        if(imageFiles != null) {
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
        CreateArticleResponse createArticleResponse = new CreateArticleResponse(id, savedArticle.getTitle(), savedArticle.getContent(),
                savedArticle.getCategory(), savedArticle.getPostDate(), savedArticle.getLikes(),
                savedArticle.getUser().getUsername());
        return createArticleResponse;
    }
}
