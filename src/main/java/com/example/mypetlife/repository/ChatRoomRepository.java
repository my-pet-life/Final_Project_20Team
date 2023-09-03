package com.example.mypetlife.repository;


import com.example.mypetlife.entity.ChatRoom;
import com.example.mypetlife.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByChatRoomUser(User user);
}
