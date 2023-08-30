package com.example.mypetlife.dto.calendar;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MessageDto {
    String to;
    String content;
}
