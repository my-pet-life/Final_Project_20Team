package com.example.mypetlife.controller.calendar;

import com.example.mypetlife.dto.calendar.MessageDto;
import com.example.mypetlife.dto.calendar.SmsResponseDto;
import com.example.mypetlife.service.calendar.SmsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@RestController
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/sms/send")
    public SmsResponseDto sendSms(@RequestBody MessageDto dto)
            throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        SmsResponseDto response = smsService.sendSms(dto);
        return response;
    }
}
