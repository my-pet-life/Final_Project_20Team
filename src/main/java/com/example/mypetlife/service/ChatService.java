package com.example.mypetlife.service;

import com.example.mypetlife.dto.chat.ChatMessageDto;
import com.example.mypetlife.dto.chat.ChatMessageListDto;
import com.example.mypetlife.entity.ChatRoom;
import com.example.mypetlife.entity.Message;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.repository.ChatRoomRepository;
import com.example.mypetlife.repository.MessageRepository;
import com.example.mypetlife.repository.UserRepository;
import com.example.mypetlife.webSocket.MsgRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;
    private MsgRoom msgRoom;
    private Map<Long, MsgRoom> msgRooms;
    private Map<WebSocketSession, Long> sessionMap;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;

    @PostConstruct
    private void init(){
        msgRooms = new LinkedHashMap<>();
        sessionMap = new LinkedHashMap<>();
    }

    public MsgRoom findRoomById(Long roomId){
        return msgRooms.get(roomId);
    }

    public void addSession(WebSocketSession webSocketSession, Long roomId){
        this.sessionMap.put(webSocketSession, roomId);
    }

    public void deleteSession(WebSocketSession webSocketSession){
        Long key = this.sessionMap.get(webSocketSession);
        MsgRoom msgRoom = this.msgRooms.get(key);

        msgRoom.deleteSession(webSocketSession);
        this.sessionMap.remove(webSocketSession);

        return;
    }

    @Transactional
    public String createRoom(User user){
        Optional<ChatRoom> optionalRoom = chatRoomRepository.findByChatRoomUser(user);
        if(optionalRoom.isEmpty()) {
            ChatRoom newRoom = ChatRoom.builder()
                    .chatRoomUser(user)
                    .build();
            chatRoomRepository.save(newRoom);

            MsgRoom msgRoom = MsgRoom.builder()
                    .id(newRoom.getId())
                    .email(user.getEmail())
                    .build();
            msgRooms.put(msgRoom.getId(), msgRoom);
            return "채팅방이 생성되었습니다. 채팅방 넘버 : "+newRoom.getId();
        }else{
            throw new CustomException(ErrorCode.ALREADY_EXIST_CHATROOM);
        }

    }

    @Transactional
    public void saveMessage(ChatMessageDto chatMessageDto, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getRoomId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        Message newMessage = Message.builder()
                .content(chatMessageDto.getMessage())
                .sendTime(LocalTime.now())
                .chatRoom(chatRoom)
                .receiver(user)
                .build();

        messageRepository.save(newMessage);

    }

    public <T> void sendMessage(WebSocketSession session, T message){
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<ChatMessageListDto> readMessage(Long roomId){

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        chatRoom.getMessages();

        List<ChatMessageListDto> chatMessageList = new ArrayList<>();

        for (Message message:chatRoom.getMessages()) {
            ChatMessageListDto dto = ChatMessageListDto.createDto(message);
            chatMessageList.add(dto);
        }
        return chatMessageList;
    }

    // TODO 일반 유저일 경우 채팅방을 생성한 유저인지 확인, 관리자일 경우 통과
    public void userCheck(HttpServletRequest request, Long roomId){
        // 채팅방을 만든 사용자인지 확인
        String email = jwtTokenUtils.getEmailFromHeader(request);
        User user = userService.findByEmail(email);

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));
        if(user != chatRoom.getChatRoomUser()) throw new CustomException(ErrorCode.UNAUTHORIZED_CHATROOM);
        // TODO 관리자일 경우는 접근 가능

    }
}
