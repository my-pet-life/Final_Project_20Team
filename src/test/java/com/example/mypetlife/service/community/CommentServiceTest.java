package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.community.article.Article;
import com.example.mypetlife.entity.community.article.ArticleCategory;
import com.example.mypetlife.entity.community.comment.Comment;
import com.example.mypetlife.entity.user.Authority;
import com.example.mypetlife.entity.user.PetSpecies;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ArticleService articleService;

    @Autowired
    CommentService commentService;

    @Test
    void 댓글_등록() {

        //given
        User user = User.createUser("kim", "kim@naver.com", passwordEncoder.encode("1111"),
                "01012341234", "20220102", PetSpecies.DOG, Authority.ROLE_USER);
        userService.register(user);
        User findUser = userService.findByEmail("kim@naver.com");

        Article article = Article.createArticle("안녕하세요", "처음 가입합니다~", ArticleCategory.CHAT, findUser);
        articleService.saveArticle(article);

        //when
        Comment comment = Comment.createComment("댓글 등록", findUser, article);
        commentService.saveComment(comment);

        //then
        Assertions.assertEquals(commentService.findById(comment.getId()), comment);
    }
}