package com.example.mypetlife.entity.hospital.address;

import jakarta.persistence.Embeddable;

@Embeddable
public class GyeonggiDoAddress {
    // 도
    private String city;

    // 시
    private String country;

    // 군, 구
    private String district;

    // 도로명
    private String street;

    // 도로명 번호
    private String hospitalStreetNumber;

    protected GyeonggiDoAddress() {
    }

    // 경기도 주소 메소드
    public GyeonggiDoAddress(final String city, final String country,
                             final String district, final String street, final String hospitalStreetNumber) {
        this.city = city;
        this.country = country;
        this.district = district;
        this.street = street;
        this.hospitalStreetNumber = hospitalStreetNumber;
    }
}
