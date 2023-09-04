package com.example.mypetlife.dto.community.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAndUpdateCommentRequest {

    @NotBlank(message = "댓글을 입력하세요")
    private String content;
}
