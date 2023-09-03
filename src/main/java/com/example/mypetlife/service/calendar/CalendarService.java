package com.example.mypetlife.service.calendar;

import com.example.mypetlife.dto.calendar.alarm.AlarmScheduleListDto;
import com.example.mypetlife.dto.calendar.alarm.MessageDto;
import com.example.mypetlife.dto.calendar.alarm.SmsResponseDto;
import com.example.mypetlife.dto.calendar.schedule.*;
import com.example.mypetlife.entity.Calendar;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.repository.CalendarRepository;
import com.example.mypetlife.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service @Slf4j @RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final UserService userService;
    private final SmsService smsService;
    private final JwtTokenUtils jwtTokenUtils;

    // TODO 일정 등록
    public void create(HttpServletRequest request, ScheduleRequestDto dto)
            throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        Calendar calendar = new Calendar();
        User user = userService.findByEmail(jwtTokenUtils.getEmailFromHeader(request));
        calendar.setUserId(user);
        calendar.setDate(dto.getDate());
        calendar.setStartTime(dto.getStartTime());
        calendar.setEndTime(dto.getEndTime());
        calendar.setTitle(dto.getTitle());
        calendar.setContent(dto.getContent());
        calendar.setLocation(dto.getLocation());
        calendar.setAlarm(dto.getAlarm());
        if(dto.getAlarm() != null) {
            calendar.setReserveId(sendMessage(dto, user.getPhone()));
        }

        log.info(calendar.toString());
        calendarRepository.save(calendar);
    }

    // TODO 일정 등록 시, 알람이 설정되어 있으면 예약 메세지 전송
    public String sendMessage(ScheduleRequestDto dto, String phoneNumber)
            throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {

        // 메세지 템플릿 만들기
        String str = String.format("%s, %s 에 시작하는 일정 [ %s ] 이 %d분 남았습니다.(my-pet-life)",
                dto.getDate(), dto.getStartTime(), dto.getTitle(), dto.getAlarm());
        log.info("알림 메세지: " + str);

        // 알림 시간 설정
        String messageDate = calDate(dto);
        log.info("알림 전송 예약 시간:" + messageDate);

        //  메세지 전송 후 아이디 리턴
        MessageDto messageDto = new MessageDto();
        messageDto.setTo(phoneNumber);
        messageDto.setContent(str);
        SmsResponseDto smsResponseDto = smsService.sendSms(messageDto, messageDate);
        log.info(String.valueOf(smsResponseDto));
        return smsResponseDto.getRequestId();
    }

    public String calDate(ScheduleRequestDto dto) {
        // 등록할 날짜, 시간 - 알림 시간 계산한 값을 String 반환
        LocalTime startTime = LocalTime.parse(dto.getStartTime());  // 시작 시간 파싱
        Duration alarmDuration = Duration.ofMinutes(dto.getAlarm());  // 알림 시간을 분 단위로 변환 (15 30, 60 120, 180, 360, 1440 올 수 있음)
        LocalDateTime scheduleDateTime = dto.getDate().atTime(startTime); // 일정 시작 날짜와 시간 조합
        LocalDateTime notificationTime = scheduleDateTime.minus(alarmDuration); // 알림 시간 전의 시간 계산

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // 결과 포맷 형식 지정
        return notificationTime.format(formatter); // 알림 문자 발송 시간을 지정된 포맷으로 반환
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
    public void updateSchedule(HttpServletRequest request, Long scheduleId, UpdatedScheduleDto dto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
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

        Calendar newCalendar = calendarRepository.findById(scheduleId).get();

        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto();
        scheduleRequestDto.setDate(newCalendar.getDate());
        scheduleRequestDto.setStartTime(newCalendar.getStartTime());
        scheduleRequestDto.setEndTime(newCalendar.getEndTime());
        scheduleRequestDto.setTitle(newCalendar.getTitle());
        scheduleRequestDto.setContent(newCalendar.getContent());
        scheduleRequestDto.setLocation(newCalendar.getLocation());
        scheduleRequestDto.setAlarm(newCalendar.getAlarm());

        if(newCalendar.getReserveId() != null) {
            smsService.deleteSms(newCalendar.getReserveId());
            newCalendar.setReserveId(null);
            calendarRepository.save(newCalendar);
            log.info("예약 메세지 알림이 취소되었습니다.");
        }

        if (newCalendar.getAlarm() != 0) {
            newCalendar.setReserveId(sendMessage(scheduleRequestDto, user.getPhone()));
            calendarRepository.save(newCalendar);
            log.info("예약 메세지 알림이 등록되었습니다.");
        }
    }

    private <T> void updateFieldIfNotNull(T newValue, Consumer<T> updateFunction) {
        if (newValue != null) updateFunction.accept(newValue);
    }


    // TODO 일정 삭제
    public void deleteSchedule(HttpServletRequest request, Long scheduleId)
            throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        Calendar calendar = calendarRepository.findById(scheduleId).orElseThrow(()
                -> new CustomException(ErrorCode.NOT_FOUND_SCHEDULE));
        User user = userService.findByEmail(jwtTokenUtils.getEmailFromHeader(request));
        if(!user.equals(calendar.getUserId()))
            throw new CustomException(ErrorCode.UNAUTHORIZED);

        smsService.deleteSms(calendar.getReserveId());
        calendarRepository.delete(calendar);
    }

}