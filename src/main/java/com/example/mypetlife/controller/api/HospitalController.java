package com.example.mypetlife.controller.api;

import com.example.mypetlife.dto.hospital.DistrictResponseDto;
import com.example.mypetlife.dto.hospital.gyeonggiDo.GyeonggiHospitalCountryResponseDto;
import com.example.mypetlife.dto.hospital.gyeonggiDo.GyeonggiHospitalInfoResponseDto;
import com.example.mypetlife.dto.hospital.seoul.SeoulHospitalInfoResponseDto;
import com.example.mypetlife.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospitals")
public class HospitalController {
    private final HospitalService hospitalService;

    // 시 도 필터
    @GetMapping("/{city}")
    public List<DistrictResponseDto> hospitalCityFilter(@PathVariable(value = "city") final String city) {
        return hospitalService.getHospitalCity(city);
    }

    // 서울 ~구 동물병원 객체 반환
    @GetMapping("/seoul/{city}/{district}")
    public List<SeoulHospitalInfoResponseDto> seoulHospitalDistrictFilter(@PathVariable final String city,
                                                                          @PathVariable final String district) {
        return hospitalService.getSeoulHospital(city, district);
    }

    // 경기도 ~시 동물병원 객체 반환 ~구 필터
    @GetMapping("/gyeonggi/{city}/{country}")
    public List<GyeonggiHospitalCountryResponseDto> gyeonggiDoHospitalCountryFilter(@PathVariable final String city,
                                                                                    @PathVariable final String country) {
        return hospitalService.getGyeonggiDoHospitalDistrict(city, country);
    }

    // 경기도 ~시 ~구 동물병원 객체 반환
    @GetMapping("/gyeonggi/{city}/{country}/{district}")
    public List<GyeonggiHospitalInfoResponseDto> gyeonggiHospitalStreetFilter(@PathVariable final String city,
                                                                              @PathVariable final String country,
                                                                              @PathVariable final String district) {
        return hospitalService.getGyeonggiDoHospital(city, country, district);
    }
}
