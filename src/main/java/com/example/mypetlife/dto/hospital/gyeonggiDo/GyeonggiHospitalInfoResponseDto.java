package com.example.mypetlife.dto.hospital.gyeonggiDo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GyeonggiHospitalInfoResponseDto {

    private String city;
    private String country;
    private String district;
    private String street;
    private String hospitalStreetNumber;
    private String hospitalName;
    private Integer reviewCount;


    public GyeonggiHospitalInfoResponseDto(final String city, final String country, final String district,
                                           final String street, final String hospitalStreetNumber,
                                           final Integer reviewCount, final String hospitalName) {
        this.city = city;
        this.country = country;
        this.district = district;
        this.street = street;
        this.hospitalStreetNumber = hospitalStreetNumber;
        this.hospitalName = hospitalName;
        this.reviewCount = reviewCount;
    }
}
