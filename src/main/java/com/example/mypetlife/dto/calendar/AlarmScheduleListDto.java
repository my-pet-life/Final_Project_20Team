package com.example.mypetlife.dto.calendar;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlarmScheduleListDto {
    private LocalDate date;
    private Integer alarm;
    private String startTime;
    private String endTime;
    private String title;
}
