package com.example.mypetlife.dto.community.article;

import com.example.mypetlife.entity.community.article.ArticleCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateArticleRequest {

    @NotBlank(message = "제목을 입력하세요")
    private String title;

    @NotBlank(message = "글을 입력하세요")
    private String content;

    @NotBlank(message = "카테고리를 선택하세요")
    private String category;
}
