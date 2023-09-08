package com.example.mypetlife.dto.calendar.schedule;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleResponseDto {
    private Long id;
    private LocalDate date;
    private String startTime;
    private String endTime;
    private String title;
    private String content;
    private String location;
    private Boolean isAlarm;
}
