package com.example.mypetlife.dto.calendar.schedule;

import lombok.Builder;
import lombok.Data;

@Data
public class ScheduleListResponseDto {
    private String startTime;
    private String endTime;
    private String title;
    private String location;

}