package com.example.mypetlife.dto.community.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateArticleRequest {

    @NotBlank(message = "제목을 입력하세요")
    private final String title;
    @NotBlank(message = "글을 입력하세요")
    private final String content;
    @NotBlank(message = "카테고리를 선택하세요")
    private final String category;
    private List<String> tags = new ArrayList<>();
}
