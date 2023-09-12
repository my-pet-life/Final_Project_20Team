package com.example.mypetlife.entity.hospital;

import com.example.mypetlife.entity.hospital.address.GyeonggiDoAddress;
import com.example.mypetlife.entity.review.GyeonggiReview;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gyeonggido_hospital")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GyeonggiDoHospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gyeonggido_hospital_id")
    private Long id;

    // 동물 병원 이름
    @Column(name = "hospital_name")
    private String hospitalName;

    @Embedded
    private GyeonggiDoAddress gyeonggiDoAddress;

    // 정상 / 폐업
    @Column(name = "hospital_operation")
    private String hospitalOperation;

    @Column(name = "next_review_id")
    private Long nextReviewId = 1L; // 다음 리뷰의 ID를 관리, 초기값은 1

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @OneToMany(mappedBy = "gyeonggiDoHospitalId")
    private List<GyeonggiReview> reviewEntities = new ArrayList<>();

    public static GyeonggiDoHospital saveHospitalInfo(final String hospitalName,
                                                      final GyeonggiDoAddress gyeonggiDoAddress, final String hospitalOperation) {
        GyeonggiDoHospital gyeonggiDoHospital = new GyeonggiDoHospital();
        gyeonggiDoHospital.hospitalName = hospitalName;
        gyeonggiDoHospital.gyeonggiDoAddress = gyeonggiDoAddress;
        gyeonggiDoHospital.hospitalOperation = hospitalOperation;

        return gyeonggiDoHospital;
    }

    public void increaseReviewCount() {
        this.reviewCount++;
    }

    public void increaseNextReviewCount() {
        // 다음 리뷰의 ID를 증가시킴
        this.nextReviewId++;
    }
}