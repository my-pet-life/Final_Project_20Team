package com.example.mypetlife.dto.calendar;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data @Builder
public class UpdatedScheduleDto {
    private LocalDate date;
    private String startTime;
    private String endTime;
    private String title;
    private String content;
    private String location;
    private Integer alarm;
}
