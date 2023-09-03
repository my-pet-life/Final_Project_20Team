package com.example.mypetlife.dto.calendar.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleRequestDto {
    @NotNull(message = "날짜를 입력하세요")
    private LocalDate date;
    @NotBlank(message = "시작 시간을 입력하세요.")
    private String startTime;
    @NotBlank(message = "종료 시간을 입력하세요.")
    private String endTime;
    @NotBlank(message = "제목을 입력하세요.")
    private String title;
    private String content;
    private String location;
    private Integer alarm;
}