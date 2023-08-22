package com.example.mypetlife.controller;

import com.example.mypetlife.dto.ResponseDto;
import com.example.mypetlife.dto.calendar.*;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.service.CalendarService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    @PostMapping
    public ResponseDto create(HttpServletRequest request, @Valid @RequestBody ScheduleRequestDto dto) {
        calendarService.create(request, dto);
        return responseDto("일정이 등록되었습니다.");
    }

    @GetMapping("/readList")
    public List<ScheduleListResponseDto> readDate(HttpServletRequest request, @RequestParam("date") String date) {
        return calendarService.readDateSchedule(request, date);
    }

    @GetMapping("/{scheduleId}")
    public ScheduleResponseDto  readSchedule(HttpServletRequest request, @PathVariable("scheduleId") Long scheduleId){
        return calendarService.readSchedule(request, scheduleId);
    }

    @GetMapping("/readall")
    public List<ScheduleAllListResponseDto> readAll(HttpServletRequest request) {
        return calendarService.readAllSchedule(request);
    }

    @GetMapping("/readall/alarm")
    public List<AlarmScheduleListDto> readAllAlarmSchedule(HttpServletRequest request) {
        return calendarService.readAllAlarmSchedule(request);
    }

    @PutMapping("/{scheduleId}")
    public ResponseDto updateSchedule(HttpServletRequest request, @PathVariable("scheduleId") Long scheduleId, @RequestBody UpdatedScheduleDto dto){
        calendarService.updateSchedule(request, scheduleId, dto);
        return responseDto("수정이 완료되었습니다.");
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseDto deleteSchedule(HttpServletRequest request, @PathVariable("scheduleId") Long scheduleId) {
        calendarService.deleteSchedule(request, scheduleId);
        return responseDto("삭제가 완료되었습니다.");
    }

    public ResponseDto responseDto(String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(message);
        return responseDto;
    }
}