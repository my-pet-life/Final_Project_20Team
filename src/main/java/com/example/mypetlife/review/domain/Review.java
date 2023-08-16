package com.example.mypetlife.review.domain;

import com.example.mypetlife.hospital.domain.Hospital;
import com.example.mypetlife.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "therapy")
    private String therapy;

    @Column(name = "price")
    private Integer price;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @Column(name = "star_rating")
    private Integer starRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviews")
    private Hospital hospitalId;
}
