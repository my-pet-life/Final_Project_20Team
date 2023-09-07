package com.example.mypetlife.entity.review;

import com.example.mypetlife.entity.hospital.GyeonggiDoHospital;
import com.example.mypetlife.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Entity
@Table(name = "gyeonggi_reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE reviews SET deleted_at = CURRENT_TIMESTAMP WHERE gyeonggido_hospital_id = ?")
public class GyeonggiReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "next_review_id")
    private Long nextReviewId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

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

    @Column(name = "deleted_at")
    private LocalDateTime deleteDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gyeonggido_hospital_id")
    private GyeonggiDoHospital gyeonggiDoHospitalId;

    public void setUser(final User user) {
        this.userId = user;
        user.getGyeonggiReviews().add(this);
    }

    public void setGyeonggiDoHospital(final GyeonggiDoHospital hospital) {
        this.gyeonggiDoHospitalId = hospital;
        hospital.getReviewEntities().add(this);
    }

    public GyeonggiReview(final Long nextReviewId, final String title, final String content, final String therapy, final Integer price,
                          final LocalDateTime reviewDate, final Integer starRating) {
        this.nextReviewId = nextReviewId;
        this.title = title;
        this.content = content;
        this.therapy = therapy;
        this.price = price;
        this.reviewDate = reviewDate;
        this.starRating = starRating;
    }

    @Builder
    public void modifyReview(final String title, final String content, final String therapy, final Integer price,
                             final LocalDateTime reviewDate, final Integer starRating) {

        this.title = title;
        this.content = content;
        this.therapy = therapy;
        this.price = price;
        this.reviewDate = reviewDate;
        this.starRating = starRating;
    }
}