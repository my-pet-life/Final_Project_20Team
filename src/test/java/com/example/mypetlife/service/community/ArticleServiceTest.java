package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.community.article.Article;
import com.example.mypetlife.entity.community.article.ArticleCategory;
import com.example.mypetlife.entity.user.Authority;
import com.example.mypetlife.entity.user.PetSpecies;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    LikeArticleService likeArticleService;

    @Test
    void 게시글_저장() {

        //given
        User user = User.createUser("kim", "kim@naver.com", passwordEncoder.encode("1111"),
                "01012341234", "20220102", PetSpecies.DOG, Authority.ROLE_USER);

        userService.register(user);
        User findUser = userService.findByEmail("kim@naver.com");

        //when
        Article article = Article.createArticle("안녕하세요", "처음 가입합니다~", ArticleCategory.CHAT, findUser);
        Long id = articleService.saveArticle(article);

        //then
        Assertions.assertEquals(article, articleService.findById(id));
    }

    @Test
    void 전체_게시글_조회() {

        //given
        User user = User.createUser("kim", "kim@naver.com", passwordEncoder.encode("1111"),
                "01012341234", "20220102", PetSpecies.DOG, Authority.ROLE_USER);

        userService.register(user);
        User findUser = userService.findByEmail("kim@naver.com");

        //when
        Article article1 = Article.createArticle("안녕하세요", "처음 가입합니다~", ArticleCategory.CHAT, findUser);
        articleService.saveArticle(article1);

        Article article2 = Article.createArticle("강아지 사료 후기", "쿠팡에서 산 사료 좋네요", ArticleCategory.REVIEW, findUser);
        articleService.saveArticle(article2);

        // then
        Assertions.assertEquals(articleService.findAll().size(), 2);
    }

    @Test
    void 게시글_단일_조회() {

        //given
        User user = User.createUser("kim", "kim@naver.com", passwordEncoder.encode("1111"),
                "01012341234", "20220102", PetSpecies.DOG, Authority.ROLE_USER);
        userService.register(user);
        User findUser = userService.findByEmail("kim@naver.com");


        Article article = Article.createArticle("안녕하세요", "처음 가입합니다~", ArticleCategory.CHAT, findUser);
        Long id = articleService.saveArticle(article);

        //when, then
        Assertions.assertEquals(article, articleService.findById(id));
    }

    @Test
    void 게시글_좋아요() {

        //given
        User user = User.createUser("kim", "kim@naver.com", passwordEncoder.encode("1111"),
                "01012341234", "20220102", PetSpecies.DOG, Authority.ROLE_USER);
        userService.register(user);
        User findUser = userService.findByEmail("kim@naver.com");


        Article article = Article.createArticle("안녕하세요", "처음 가입합니다~", ArticleCategory.CHAT, findUser);
        Long id = articleService.saveArticle(article);

        //when
        likeArticleService.saveLike(findUser, id);

        //then
        Assertions.assertEquals(1, article.getLikeArticles().size());
    }
}