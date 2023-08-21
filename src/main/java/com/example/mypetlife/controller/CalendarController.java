package com.example.mypetlife.controller;

import com.example.mypetlife.dto.ResponseDto;
import com.example.mypetlife.dto.calendar.ScheduleResponseDto;
import com.example.mypetlife.dto.calendar.ScheduleRequestDto;
import com.example.mypetlife.service.CalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    @PostMapping
    public ResponseDto create(Authentication authentication, @Valid @RequestBody ScheduleRequestDto dto) {
        log.info(authentication.toString());
        calendarService.create(authentication, dto);
        return responseDto("일정이 등록되었습니다.");
    }

    @GetMapping
    public List<ScheduleResponseDto> readDate(Authentication authentication, @RequestParam("date") String date) {
        return calendarService.readDateSchedule(authentication, date);
    }

    public ResponseDto responseDto(String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(message);
        return responseDto;
    }
}