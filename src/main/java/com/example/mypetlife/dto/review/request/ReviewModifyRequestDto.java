package com.example.mypetlife.dto.review.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewModifyRequestDto {
    private String title;
    private String content;
    private String therapy;
    private Integer price;
    private Integer starRating;
}
