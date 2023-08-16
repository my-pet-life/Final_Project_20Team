package com.example.mypetlife.user.domain;

import com.example.mypetlife.calendar.domain.Calendar;
import com.example.mypetlife.review.domain.Review;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String username;
    private String email;
    private String password;
    private String phone;
    private String birthDate;
    private String petSpices;
    private LocalDate created_at;

    @OneToMany(mappedBy = "userId")
    private List<Calendar> calendars = new ArrayList<>();

    @OneToMany(mappedBy = "userId")
    private List<Review> reviews = new ArrayList<>();
}
