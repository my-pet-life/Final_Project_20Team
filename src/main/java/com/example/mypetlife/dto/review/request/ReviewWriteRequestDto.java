package com.example.mypetlife.dto.review.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewWriteRequestDto {
    private String title;
    private String content;
    private String therapy;
    private Integer price;
    private Integer starRating;
}
