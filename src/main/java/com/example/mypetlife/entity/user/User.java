package com.example.mypetlife.entity.user;

import com.example.mypetlife.entity.Calendar;
import com.example.mypetlife.entity.ChatRoom;
import com.example.mypetlife.entity.Message;
import com.example.mypetlife.entity.Review;
import com.example.mypetlife.entity.article.Article;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
public class User {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;
    private String phone;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "pet_species")
    private String petSpecies;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Article> articles;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Message> receiveMessages;

    @OneToMany(mappedBy ="chatRoomUser")
    private List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "userId")
    private List<Calendar> calendars = new ArrayList<>();

    @OneToMany(mappedBy = "userId")
    private List<Review> reviews = new ArrayList<>();

    //==생성 메서드==//
    public static User createUser(String username, String email, String password, String phone, String birthDate, String petSpecies) {

        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.phone = phone;
        user.birthDate = birthDate;
        user.petSpecies = petSpecies;
        user.createdAt = LocalDateTime.now();

        return user;
    }
}
