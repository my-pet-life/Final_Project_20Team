package com.example.mypetlife.entity;

import com.example.mypetlife.entity.article.Article;
import jakarta.persistence.*;

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

    //==생성 메서드==//
    public static User createUser(String username, String email, String password, String phone, String birthDate, String petSpices) {

        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.phone = phone;
        user.birthDate = birthDate;
        user.petSpices = petSpices;
        user.createdAt = LocalDateTime.now();

        return user;
    }
}
