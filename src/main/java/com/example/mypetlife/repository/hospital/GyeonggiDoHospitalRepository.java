package com.example.mypetlife.repository.hospital;

import com.example.mypetlife.entity.hospital.GyeonggiDoHospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GyeonggiDoHospitalRepository extends JpaRepository<GyeonggiDoHospital, Long> {

    @Query("SELECT gyeonggi FROM GyeonggiDoHospital gyeonggi WHERE gyeonggi.gyeonggiDoAddress.city = :city")
    List<GyeonggiDoHospital> findByGyeonggiDoAddress_City(@Param("city") final String city);


    @Query("SELECT gyeonggi FROM GyeonggiDoHospital gyeonggi WHERE gyeonggi.gyeonggiDoAddress.city = :city " +
            "AND gyeonggi.gyeonggiDoAddress.country = :country")
    List<GyeonggiDoHospital> findByGyeonggiDoAddress_CityAndGyeonggiDoAddress_Country(@Param("city") final String city,
                                                                                      @Param("country") final String country);

    @Query("SELECT gyeonggi FROM GyeonggiDoHospital gyeonggi WHERE gyeonggi.gyeonggiDoAddress.city = :city " +
            "AND gyeonggi.gyeonggiDoAddress.country = :country " +
            "AND gyeonggi.gyeonggiDoAddress.district = :district")
    List<GyeonggiDoHospital> findByGyeonggiDoAddress_CityAndGyeonggiDoAddress_CountryAndAndGyeonggiDoAddress_District
            (@Param("city") final String city,
             @Param("country") final String country,
             @Param("district") final String district);

    @Query("SELECT gyeonggi FROM GyeonggiDoHospital gyeonggi " +
            "WHERE gyeonggi.gyeonggiDoAddress.city = :city " +
            "AND gyeonggi.gyeonggiDoAddress.country = :country " +
            "AND gyeonggi.gyeonggiDoAddress.district = :district " +
            "AND gyeonggi.hospitalName = :hospitalName")
    Optional<GyeonggiDoHospital> findByGyeonggiDoAddress_CityAndGyeonggiDoAddress_CountryAndAndGyeonggiDoAddress_DistrictAndHospitalName(
            @Param("city") final String city,
            @Param("country") final String country,
            @Param("district") final String district,
            @Param("hospitalName") final String hospitalName);
}