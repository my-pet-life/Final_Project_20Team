package com.example.mypetlife.entity.user;

import com.example.mypetlife.entity.Calendar;
import com.example.mypetlife.entity.ChatRoom;
import com.example.mypetlife.entity.Message;
import com.example.mypetlife.entity.community.article.Article;
import com.example.mypetlife.entity.community.article.LikeArticle;
import com.example.mypetlife.entity.review.GyeonggiReview;
import com.example.mypetlife.entity.review.SeoulReview;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String email;

    private String password;

    private String phone;

    private String birthDate;

    @Enumerated(EnumType.STRING)
    private PetSpecies petSpecies;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "user")
    private List<Article> articles;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Message> receiveMessages;

    @OneToMany(mappedBy = "userId")
    private List<Calendar> calendars = new ArrayList<>();

    @OneToMany(mappedBy = "userId")
    private List<GyeonggiReview> gyeonggiReviews = new ArrayList<>();

    @OneToMany(mappedBy = "userId")
    private List<SeoulReview> seoulReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<LikeArticle> likeArticles = new ArrayList<>();

    @OneToMany(mappedBy ="chatRoomUser")
    private List<ChatRoom> chatRooms;

    //==생성 메서드==//
    public static User createUser(String username, String email, String password, String phone,
                                  String birthDate, PetSpecies petSpecies, Authority authority) {

        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.phone = phone;
        user.birthDate = birthDate;
        user.petSpecies = petSpecies;
        user.createdAt = LocalDateTime.now();
        user.authority = authority;

        return user;
    }

    //==연관관계 편의 메서드==//
    public void addLike(LikeArticle likeArticle) {

        likeArticle.setUser(this);
        this.likeArticles.add(likeArticle);
    }
}
