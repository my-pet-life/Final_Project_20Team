package com.example.mypetlife.dto.hospital;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DistrictResponseDto {
    private List<String> district;

    public DistrictResponseDto(final List<String> district) {
        this.district = district;
    }
}
