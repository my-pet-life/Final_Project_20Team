package com.example.mypetlife.repository.community;

import com.example.mypetlife.entity.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
