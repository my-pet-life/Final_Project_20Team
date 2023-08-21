package com.example.mypetlife.entity.hospital;

import com.example.mypetlife.entity.Review;
import com.example.mypetlife.entity.hospital.address.GyeonggiDoAddress;
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

    @OneToMany(mappedBy = "gyeonggiDoHospitalId")
    private List<Review> reviews = new ArrayList<>();

    public static GyeonggiDoHospital saveHospitalInfo(final String hospitalName,
                                                      final GyeonggiDoAddress gyeonggiDoAddress, final String hospitalOperation) {
        GyeonggiDoHospital gyeonggiDoHospital = new GyeonggiDoHospital();
        gyeonggiDoHospital.hospitalName = hospitalName;
        gyeonggiDoHospital.gyeonggiDoAddress = gyeonggiDoAddress;
        gyeonggiDoHospital.hospitalOperation = hospitalOperation;

        return gyeonggiDoHospital;
    }
}
