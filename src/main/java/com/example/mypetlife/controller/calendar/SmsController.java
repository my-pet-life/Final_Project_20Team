package com.example.mypetlife.controller.calendar;

import com.example.mypetlife.dto.calendar.alarm.MessageDto;
import com.example.mypetlife.dto.calendar.alarm.SmsResponseDto;
import com.example.mypetlife.dto.calendar.schedule.ScheduleRequestDto;
import com.example.mypetlife.service.calendar.SmsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name = "SMS Test", description = "반려 동물 일정 알림 메세지 전송 테스트 API")
public class SmsController {
    private final SmsService smsService;

    // TODO 메세지 전송 테스트
    @PostMapping("/sms/send/test")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = MessageDto.class))
    )
    public SmsResponseDto sendSms(@RequestBody MessageDto dto)
            throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime tenMinutesLater = currentTime.plusMinutes(15);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedTime = tenMinutesLater.format(formatter);

        log.info("reserved Time: " + formattedTime);
        return smsService.sendSms(dto, formattedTime);
    }
}
