package com.example.mypetlife.webSocket;

import com.example.mypetlife.dto.chat.ChatMessageDto;
import com.example.mypetlife.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class MsgRoom {
    private Long id;
    private String email;
    private static Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public MsgRoom(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public void handleActions(WebSocketSession session, ChatMessageDto chatMessageDto, ChatService chatService, String email) {
        // TODO 권한 확인
        if (chatMessageDto.getType().equals(ChatMessageDto.MessageType.ENTER)) {
            sessions.add(session);
            chatMessageDto.setMessage(email + "님이 입장하셨습니다. 문의 내용을 보내주세요.");
        }
        chatService.saveMessage(chatMessageDto, email);
        sendMessage(chatMessageDto, chatService);
    }

    public <T> void sendMessage(ChatMessageDto message, ChatService chatService) {
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }

    public static void deleteSession(WebSocketSession webSocketSession) {
        sessions.remove(webSocketSession);
    }
}
