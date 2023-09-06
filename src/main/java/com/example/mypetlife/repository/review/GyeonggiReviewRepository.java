package com.example.mypetlife.repository.review;


import com.example.mypetlife.entity.hospital.GyeonggiDoHospital;
import com.example.mypetlife.entity.review.GyeonggiReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GyeonggiReviewRepository extends JpaRepository<GyeonggiReview, Long> {
    @Query("SELECT review FROM GyeonggiReview review WHERE review.gyeonggiDoHospitalId = :gyeonggiDoHospitalId " +
            "AND review.nextReviewId = :nextReviewId ")
    Optional<GyeonggiReview> findByGyeonggiDoHospitalIdAndNextReviewId
            (@Param("gyeonggiDoHospitalId") final GyeonggiDoHospital gyeonggiDoHospital, @Param("nextReviewId") final Long nextReviewId);
}
