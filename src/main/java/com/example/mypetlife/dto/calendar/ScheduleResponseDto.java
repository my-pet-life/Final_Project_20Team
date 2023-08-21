package com.example.mypetlife.dto.calendar;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleResponseDto {
    private LocalDate date;
    private String startTime;
    private String endTime;
    private String title;
    private String content;
    private String location;
    private Integer alarm;
}