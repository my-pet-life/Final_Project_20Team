package com.example.mypetlife.webSocket;

import com.example.mypetlife.dto.chat.ChatMessageDto;
import com.example.mypetlife.entity.user.Authority;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.repository.UserRepository;
import com.example.mypetlife.service.ChatService;
import lombok.*;
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
        if (chatMessageDto.getType().equals(ChatMessageDto.MessageType.ENTER)) {
            sessions.add(session);
            if(email.equals("admin@naver.com")){
                chatMessageDto.setMessage("관리자가 입장하였습니다. 잠시 기다려주세요.");
            }else{
                chatMessageDto.setMessage(email+"님이 입장하였습니다. 문의 내용을 남겨주세요");
            }
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
