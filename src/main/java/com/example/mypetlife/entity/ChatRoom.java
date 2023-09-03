package com.example.mypetlife.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "chatroom")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // receiver_id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "receiver_id")
    private User chatRoomUser;

    @OneToMany(mappedBy = "chatRoom")
    private List<Message> messages = new ArrayList<>();

    public void addMessage(Message message){
        this.messages.add(message);
    }
}
