package com.example.mypetlife.service.calendar;

import com.example.mypetlife.dto.calendar.*;
import com.example.mypetlife.entity.Calendar;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.repository.CalendarRepository;
import com.example.mypetlife.repository.UserRepository;
import com.example.mypetlife.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service @Slf4j @RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    // TODO 일정 등록
    public void create(HttpServletRequest request, ScheduleRequestDto dto) {
        Calendar calendar = new Calendar();
        calendar.setUserId(userService.findByEmail(jwtTokenUtils.getEmailFromHeader(request)));
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
    public List<ScheduleListResponseDto> readDateSchedule(HttpServletRequest request, String date){
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        User user = userService.findByEmail(jwtTokenUtils.getEmailFromHeader(request));

        List<ScheduleListResponseDto> scheduleList = calendarRepository.findAllByDate(localDate).stream()
                .filter(calendar -> calendar.getUserId().equals(user))
                .map(calendar -> {
                    ScheduleListResponseDto dto = new ScheduleListResponseDto();
                    dto.setStartTime(calendar.getStartTime());
                    dto.setEndTime(calendar.getEndTime());
                    dto.setTitle(calendar.getTitle());
                    dto.setLocation(calendar.getLocation());
                    return dto;
                })
                .sorted(Comparator.comparing(ScheduleListResponseDto::getStartTime))
                .collect(Collectors.toList());

        return scheduleList;
    }

    // TODO 일정 단일 조회
    public ScheduleResponseDto readSchedule(HttpServletRequest request, Long scheduleId){
        Calendar calendar = calendarRepository.findById(scheduleId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));
        User user = userService.findByEmail(jwtTokenUtils.getEmailFromHeader(request));
        if(!user.equals(calendar.getUserId()))
            throw new CustomException(ErrorCode.UNAUTHORIZED);

        ScheduleResponseDto dto = new ScheduleResponseDto();
        dto.setDate(calendar.getDate());
        dto.setTitle(calendar.getTitle());
        dto.setContent(calendar.getContent());
        dto.setStartTime(calendar.getStartTime());
        dto.setEndTime(calendar.getEndTime());
        dto.setIsAlarm(calendar.getAlarm() != null);
        dto.setLocation(calendar.getLocation());
        return dto;
    }

    // TODO 일정 전체 조회
    public List<ScheduleAllListResponseDto> readAllSchedule(HttpServletRequest request) {
        User user = userService.findByEmail(jwtTokenUtils.getEmailFromHeader(request));
        return calendarRepository.findAllByUserId(user).stream()
                .map(calendar -> {
                    ScheduleAllListResponseDto dto = new ScheduleAllListResponseDto();
                    dto.setDate(calendar.getDate());
                    dto.setStartTime(calendar.getStartTime());
                    dto.setEndTime(calendar.getEndTime());
                    dto.setTitle(calendar.getTitle());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // TODO 알림 설정한 일정 조회
    public List<AlarmScheduleListDto> readAllAlarmSchedule(HttpServletRequest request) {
        User user = userService.findByEmail(jwtTokenUtils.getEmailFromHeader(request));
        return calendarRepository.findAllByUserId(user).stream()
                .filter(calendar -> calendar.getAlarm() != null)
                .map(calendar -> {
                    AlarmScheduleListDto dto = new AlarmScheduleListDto();
                    dto.setAlarm(calendar.getAlarm());
                    dto.setDate(calendar.getDate());
                    dto.setStartTime(calendar.getStartTime());
                    dto.setEndTime(calendar.getEndTime());
                    dto.setTitle(calendar.getTitle());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // TODO 일정 수정
    public void updateSchedule(HttpServletRequest request, Long scheduleId, UpdatedScheduleDto dto) {
        Calendar calendar = calendarRepository.findById(scheduleId).orElseThrow(()
                -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));
        User user = userService.findByEmail(jwtTokenUtils.getEmailFromHeader(request));
        if(!user.equals(calendar.getUserId()))
            throw new CustomException(ErrorCode.UNAUTHORIZED);

        updateFieldIfNotNull(dto.getDate(), calendar::setDate);
        updateFieldIfNotNull(dto.getStartTime(), calendar::setStartTime);
        updateFieldIfNotNull(dto.getEndTime(), calendar::setEndTime);
        updateFieldIfNotNull(dto.getTitle(), calendar::setTitle);
        updateFieldIfNotNull(dto.getContent(), calendar::setContent);
        updateFieldIfNotNull(dto.getLocation(), calendar::setLocation);
        updateFieldIfNotNull(dto.getAlarm(), calendar::setAlarm);
        calendarRepository.save(calendar);
    }

    private <T> void updateFieldIfNotNull(T newValue, Consumer<T> updateFunction) {
        if (newValue != null) updateFunction.accept(newValue);
    }


    // TODO 일정 삭제
    public void deleteSchedule(HttpServletRequest request, Long scheduleId) {
        Calendar calendar = calendarRepository.findById(scheduleId).orElseThrow(()
                -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));
        User user = userService.findByEmail(jwtTokenUtils.getEmailFromHeader(request));
        if(!user.equals(calendar.getUserId()))
            throw new CustomException(ErrorCode.UNAUTHORIZED);

        calendarRepository.delete(calendar);
    }

}