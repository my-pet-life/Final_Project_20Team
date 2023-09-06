package com.example.mypetlife.repository.hospital;

import com.example.mypetlife.entity.hospital.SeoulHospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeoulHospitalRepository extends JpaRepository<SeoulHospital, Long> {
    // ~구 필터 쿼리
    @Query("SELECT seoul FROM SeoulHospital seoul WHERE seoul.seoulAddress.city = :city")
    List<SeoulHospital> findBySeoulAddress_City(@Param("city") final String city);

    // 사용자가 선택한 서울시 ~구 동물병원 객체 조회 쿼리
    @Query("SELECT seoul FROM SeoulHospital seoul WHERE seoul.seoulAddress.city = :city " +
            "AND seoul.seoulAddress.district = :district")
    List<SeoulHospital> findBySeoulAddress_CityAndSeoulAddress_District(@Param("city") final String city,
                                                                        @Param("district") final String district);

    @Query("SELECT seoul FROM SeoulHospital seoul " +
            "WHERE seoul.seoulAddress.city = :city " +
            "AND seoul.seoulAddress.district = :district " +
            "AND seoul.hospitalName = :hospitalName")
    Optional<SeoulHospital> findBySeoulAddress_CityAndSeoulAddress_DistrictAndHospitalName(@Param("city") final String city,
                                                                                           @Param("district") final String district,
                                                                                           @Param("hospitalName") final String hospitalName);
}

