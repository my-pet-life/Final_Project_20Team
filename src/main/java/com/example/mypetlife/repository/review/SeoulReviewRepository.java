package com.example.mypetlife.repository.review;

import com.example.mypetlife.entity.hospital.SeoulHospital;
import com.example.mypetlife.entity.review.SeoulReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SeoulReviewRepository extends JpaRepository<SeoulReview, Long> {
    @Query("SELECT review FROM SeoulReview review WHERE review.seoulHospitalId = :seoulHospitalId " +
            "AND review.nextReviewId = :nextReviewId ")
    Optional<SeoulReview> findBySeoulHospitalIdAndNextReviewId
            (@Param("seoulHospitalId") final SeoulHospital seoulHospital, @Param("nextReviewId") final Long nextReviewId);
}