package com.example.mypetlife.dto.community.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateArticleRequest {

    @NotBlank(message = "제목을 입력하세요")
    private String title;

    @NotBlank(message = "글을 입력하세요")
    private String content;

    @NotBlank(message = "카테고리를 선택하세요")
    private String category;
}
