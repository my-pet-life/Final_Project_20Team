package com.example.mypetlife.entity.hospital;

import com.example.mypetlife.entity.Review;
import com.example.mypetlife.entity.hospital.address.SeoulAddress;
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

    @OneToMany(mappedBy = "seoulHospitalId")
    private List<Review> reviews = new ArrayList<>();

    public static SeoulHospital saveHospitalInfo(final String hospitalName, final SeoulAddress seoulAddress,
                                                 final String hospitalTel, final String hospitalOperation) {
        SeoulHospital seoulHospital = new SeoulHospital();
        seoulHospital.hospitalName = hospitalName;
        seoulHospital.seoulAddress = seoulAddress;
        seoulHospital.hospitalTel = hospitalTel;
        seoulHospital.hospitalOperation = hospitalOperation;

        return seoulHospital;
    }
}