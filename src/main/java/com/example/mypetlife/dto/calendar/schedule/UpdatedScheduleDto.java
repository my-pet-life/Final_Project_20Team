package com.example.mypetlife.dto.calendar.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedScheduleDto {
    private LocalDate date;
    private String startTime;
    private String endTime;
    private String title;
    private String content;
    private String location;
    private Integer alarm;
}
