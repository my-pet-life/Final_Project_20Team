package com.example.mypetlife.repository;

import com.example.mypetlife.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
