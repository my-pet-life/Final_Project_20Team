package com.example.mypetlife.entity.hospital.address;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class SeoulAddress {
    // 시
    private String city;

    // 군, 구
    private String district;

    // 도로명
    private String street;

    // 도로명 번호
    private String hospitalStreetNumber;

    protected SeoulAddress() {
    }

    // 서울 주소 메소드
    public SeoulAddress(final String metropolitanCity, final String district,
                        final String street, final String streetNumber) {
        this.city = metropolitanCity;
        this.district = district;
        this.street = street;
        this.hospitalStreetNumber = streetNumber;
    }
}
