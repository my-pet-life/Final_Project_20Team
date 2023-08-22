package com.example.mypetlife.service.community;

import com.example.mypetlife.entity.article.Article;
import com.example.mypetlife.entity.article.LikeArticle;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.repository.community.LikeArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeArticleService {

    private final LikeArticleRepository likeArticleRepository;

    /*
     * 저장
     */
    public void saveLike(User user, Article article) {

        Optional<LikeArticle> result = likeArticleRepository.findByArticle(article)
                .stream().filter(likeArticle -> likeArticle.getUser().equals(user))
                .findFirst();

        // 이미 좋아요를 누른 상태면 좋아요 취소
        if(result.isPresent()) {
            // 좋아요 취소
            LikeArticle likeArticle = result.get();
            likeArticleRepository.delete(likeArticle);
        } else {
            // 좋아요 등록
            LikeArticle likeArticle = LikeArticle.createLikeArticle(user, article);
            likeArticleRepository.save(likeArticle);
        }
    }
}
