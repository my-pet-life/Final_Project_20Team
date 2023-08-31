package com.example.mypetlife.dto.hospital.seoul;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeoulHospitalInfoResponseDto {
    private String city;
    private String district;
    private String street;
    private String hospitalStreetNumber;
    private String hospitalName;
    private String hospitalTel;
    private Integer reviewCount;


    public SeoulHospitalInfoResponseDto(final String city, final String district,
                                        final String street, final String hospitalStreetNumber,
                                        final String hospitalTel, final Integer reviewCount,
                                        final String hospitalName) {
        this.city = city;
        this.district = district;
        this.street = street;
        this.hospitalStreetNumber = hospitalStreetNumber;
        this.hospitalName = hospitalName;
        this.hospitalTel = hospitalTel;
        this.reviewCount = reviewCount;
    }
}
