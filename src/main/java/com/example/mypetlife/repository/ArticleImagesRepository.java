package com.example.mypetlife.repository;


import com.example.mypetlife.entity.article.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleImagesRepository extends JpaRepository<ArticleImage, Long> {
}
