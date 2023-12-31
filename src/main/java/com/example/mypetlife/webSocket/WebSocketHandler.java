package com.example.mypetlife.webSocket;

import com.example.mypetlife.dto.chat.ChatMessageDto;
import com.example.mypetlife.entity.ChatRoom;
import com.example.mypetlife.entity.user.Authority;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.repository.ChatRoomRepository;
import com.example.mypetlife.repository.UserRepository;
import com.example.mypetlife.service.ChatService;
import com.example.mypetlife.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 연결되어있는 클라이언트 관리 리스트
    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    // WebSocket 최초 연결
    public void afterConnectionEstablished(WebSocketSession session){
        String email = jwtTokenUtils.getEmailFromSession(session);

        String name = userService.findByEmail(email).getUsername();

        log.info("connected with session id:{}",session.getId());
        log.info("name:{}",name);
    }


    @Override
    // 메세지를 받으면
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("received: {}",payload);
        ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
        MsgRoom msgRoom = chatService.findRoomById(chatMessageDto.getRoomId());

        // TODO 채팅방 생성한 유저거나 관리자인지 확인
        String email = jwtTokenUtils.getEmailFromSession(session);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Long roomId = chatMessageDto.getRoomId();
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        if(user.getAuthority().equals(Authority.ROLE_ADMIN) || chatRoom.getChatRoomUser().getEmail().equals(user.getEmail())){
            if(chatMessageDto.getType().toString().equals("ENTER")){
                chatService.addSession(session, msgRoom.getId());
            }
            msgRoom.handleActions(session,chatMessageDto, chatService, email);
        }else{
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHATROOM);
        }
    }


    @Override
    // 연결 종료
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        chatService.deleteSession(session);
    }
}
