package com.example.mypetlife.controller;

import com.example.mypetlife.dto.chat.ChatMessageListDto;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.repository.ChatRoomRepository;
import com.example.mypetlife.service.ChatService;
import com.example.mypetlife.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @GetMapping("/getChatting/{roomId}")
    @Operation(summary = "채팅 메세지 조회", description = "roomId를 이용하여 채팅메세지 조회")
    public List<ChatMessageListDto> startChat(
            HttpServletRequest request,
            @PathVariable("roomId") Long roomId) {
        // 유저 권한 확인
        chatService.userCheck(request, roomId);
        return chatService.readMessage(roomId);
    }

    // 채팅방 생성
    @PostMapping("/createChat")
    @Operation(summary = "채팅방 생성", description = "채팅방 생성")
    public String startChatbot(HttpServletRequest request) {
        String email = jwtTokenUtils.getEmailFromHeader(request);
        User user = userService.findByEmail(email);

        return chatService.createRoom(user);
    }


}
