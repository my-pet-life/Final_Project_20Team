package com.example.mypetlife.dto.hospital.gyeonggiDo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GyeonggiHospitalCountryResponseDto {

    private List<GyeonggiHospitalInfoResponseDto> gyeonggiHospitalInfo;
    private List<String> streetList;

    public GyeonggiHospitalCountryResponseDto(final List<GyeonggiHospitalInfoResponseDto> gyeonggiHospitalInfo,
                                              final List<String> streetList) {
        this.gyeonggiHospitalInfo = gyeonggiHospitalInfo;
        this.streetList = streetList;
    }
}
