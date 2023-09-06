package com.example.mypetlife.dto.review.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewWriteResponseDto {
    private String title;
    private String content;
    private String therapy;
    private Integer price;
    private LocalDateTime reviewDate;
    private Integer starRating;

    public ReviewWriteResponseDto(final String title, final String content, final String therapy, final Integer price,
                                  final LocalDateTime reviewDate, final Integer starRating){

        this.title = title;
        this.content = content;
        this.therapy = therapy;
        this.price = price;
        this.reviewDate = reviewDate;
        this.starRating = starRating;
    }
}
