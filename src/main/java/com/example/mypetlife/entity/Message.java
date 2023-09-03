package com.example.mypetlife.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Entity
@Getter
@Table(name = "message")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @Column(name = "send_date")
    private LocalTime sendTime;

    // sender_id
    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonIgnore
    private User sender;

    // receiver_id
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @JsonIgnore
    private User receiver;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="chatRoom_id")
    private ChatRoom chatRoom;
}
