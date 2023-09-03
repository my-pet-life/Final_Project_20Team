package com.example.mypetlife.dto.chat;

import com.example.mypetlife.entity.Message;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Builder
@Getter
public class ChatMessageListDto {
    private Long roomId;
    private Long id;
    private String content;
    private String receiver;
    private LocalTime sendTime;

    public static ChatMessageListDto createDto(Message message){
        ChatMessageListDto dto = ChatMessageListDto.builder()
                .id(message.getId())
                .roomId(message.getChatRoom().getId())
                .content(message.getContent())
                .receiver(message.getReceiver().getUsername())
                .sendTime(message.getSendTime())
                .build();
        return dto;
    }

}
