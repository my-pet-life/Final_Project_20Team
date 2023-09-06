package com.example.mypetlife.entity.hospital;

import com.example.mypetlife.entity.hospital.address.SeoulAddress;
import com.example.mypetlife.entity.review.SeoulReview;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "seoul_hospital")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeoulHospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seoul_hospital_id")
    private Long id;

    // 동물 병원 이름
    @Column(name = "hospital_name")
    private String hospitalName;
    @Embedded
    private SeoulAddress seoulAddress;

    // 동물병원 전화번호
    @Column(name = "hospital_tel")
    private String hospitalTel;

    // 정상 / 폐업
    @Column(name = "hospital_operation")
    private String hospitalOperation;

    @Column(name = "reviewCount")
    private Integer reviewCount = 0;

    @Column(name = "next_review_id")
    private Long nextReviewId = 1L; // 다음 리뷰의 ID를 관리, 초기값은 1

    @OneToMany(mappedBy = "seoulHospitalId")
    private List<SeoulReview> seoulReviewEntities = new ArrayList<>();

    public static SeoulHospital saveHospitalInfo(final String hospitalName, final SeoulAddress seoulAddress,
                                                 final String hospitalTel, final String hospitalOperation) {
        SeoulHospital seoulHospital = new SeoulHospital();
        seoulHospital.hospitalName = hospitalName;
        seoulHospital.seoulAddress = seoulAddress;
        seoulHospital.hospitalTel = hospitalTel;
        seoulHospital.hospitalOperation = hospitalOperation;

        return seoulHospital;
    }

    public void increaseReviewCount() {
        this.reviewCount++;
    }

    public void increaseNextReviewCount() {
        // 다음 리뷰의 ID를 증가시킴
        this.nextReviewId++;
    }
}