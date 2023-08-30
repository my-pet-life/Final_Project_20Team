package com.example.mypetlife.dto.calendar.alarm;

import java.util.List;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SmsRequestDto {
    String type;
    String contentType;
    String countryCode;
    String from;
    String content;
    List<MessageDto> messages;
    String reserveTime;
    String reserveTimeZone;
}
