package com.example.mypetlife.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @Column(name = "send_date")
    private LocalDateTime sendDate;

    // sender_id
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    // receiver_id
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
}
