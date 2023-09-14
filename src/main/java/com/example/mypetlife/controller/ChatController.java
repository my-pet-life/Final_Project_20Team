package com.example.mypetlife.controller;

import com.example.mypetlife.dto.chat.ChatMessageListDto;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.repository.ChatRoomRepository;
import com.example.mypetlife.service.ChatService;
import com.example.mypetlife.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;

    // 채팅 메세지 조회
    @GetMapping("/{roomId}")
    public List<ChatMessageListDto> startChat(
            HttpServletRequest request,
            @PathVariable("roomId") Long roomId) {
        // 채팅을 생성한 유저이거나 관리자일 경우
        if(chatService.userCheck(request, roomId) || chatService.adminCheck(request)) return chatService.readMessage(roomId);
        throw new CustomException(ErrorCode.UNAUTHORIZED_CHATROOM);
    }

    // 채팅방 생성
    @PostMapping("/chatRoom")
    public String startChatbot(HttpServletRequest request) {
        String email = jwtTokenUtils.getEmailFromHeader(request);
        User user = userService.findByEmail(email);

        return chatService.createRoom(user);
    }

    // 채팅방, 메세지 삭제
    @DeleteMapping("/{roomId}")
    public String deleteMessage(
            HttpServletRequest request,
            @PathVariable("roomId") Long roomId){
        // 채팅을 생성한 유저이거나 관리자일 경우 삭제가능
        if(chatService.userCheck(request, roomId) || chatService.adminCheck(request)) {
            chatService.deleteMessage(roomId);
        }else{
            throw new CustomException(ErrorCode.UNAUTHORIZED_CHATROOM);
        }
        chatService.deleteRoom(roomId);
        return "채팅방이 삭제되었습니다.";
    }


}
