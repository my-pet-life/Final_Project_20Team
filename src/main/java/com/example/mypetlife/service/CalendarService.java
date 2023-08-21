package com.example.mypetlife.service;

import com.example.mypetlife.dto.ScheduleResponseDto;
import com.example.mypetlife.dto.calendar.ScheduleRequestDto;
import com.example.mypetlife.entity.Calendar;
import com.example.mypetlife.repository.CalendarRepository;
import com.example.mypetlife.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service @Slf4j @RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;

    // TODO 일정 등록
    public void create(Authentication authentication, ScheduleRequestDto dto) {
        Calendar calendar = new Calendar();
        calendar.setUserId(userRepository.findByUsername(authentication.getName()));
        calendar.setDate(dto.getDate());
        calendar.setStartTime(dto.getStartTime());
        calendar.setEndTime(dto.getEndTime());
        calendar.setTitle(dto.getTitle());
        calendar.setContent(dto.getContent());
        calendar.setLocation(dto.getLocation());
        calendar.setAlarm(dto.getAlarm());
        log.info(calendar.toString());
        calendarRepository.save(calendar);
    }

    // TODO 일정 날짜 조회
    public List<ScheduleResponseDto> readDateSchedule(Authentication authentication, String date){
        List<ScheduleResponseDto> scheduleList = new ArrayList<>();
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

        for (Calendar calendar : calendarRepository.findAllByDate(localDate)) {
            ScheduleResponseDto dto = new ScheduleResponseDto();
            dto.setDate(calendar.getDate());
            dto.setStartTime(calendar.getStartTime());
            dto.setEndTime(calendar.getEndTime());
            dto.setTitle(calendar.getTitle());
            dto.setContent(calendar.getContent());
            dto.setLocation(calendar.getLocation());
            dto.setAlarm(calendar.getAlarm());
            scheduleList.add(dto);
        }
        return scheduleList;
    }

    // TODO 일정 단일 조회

    // TODO 일정 전체 조회
}