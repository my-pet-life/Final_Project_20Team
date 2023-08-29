package com.example.mypetlife.dto.hospital;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DistrictResponseDto {
    private String city;
    private List<String> district;

    public DistrictResponseDto(final String city, final List<String> district) {
        this.city = city;
        this.district = district;
    }
}
