package com.example.mypetlife.dto.chat;

import lombok.Data;

@Data
public class ChatMessageDto {
    public enum MessageType{
        ENTER, TALK
    }
    private MessageType type;
    private Long roomId;
    private String message;
}
