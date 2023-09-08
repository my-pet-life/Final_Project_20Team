package com.example.mypetlife.dto.calendar.schedule;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleAllListResponseDto {
    private Long id;
    private LocalDate date;
    private String startTime;
    private String endTime;
    private String title;
}
