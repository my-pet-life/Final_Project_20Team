package com.example.mypetlife.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String birthDate;
    private String petSpices;
    private LocalDateTime createdAt;

    // article
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Article> articles;

    // comment
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    // message
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Message> receiveMessages;
}
