package com.example.mypetlife.repository;

import com.example.mypetlife.entity.ChatRoom;
import com.example.mypetlife.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatRoom(ChatRoom chatRoom);
}
