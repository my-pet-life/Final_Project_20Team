package com.example.mypetlife.controller.api;

import com.example.mypetlife.dto.hospital.DistrictResponseDto;
import com.example.mypetlife.dto.hospital.gyeonggiDo.GyeonggiHospitalCountryResponseDto;
import com.example.mypetlife.dto.hospital.gyeonggiDo.GyeonggiHospitalInfoResponseDto;
import com.example.mypetlife.dto.hospital.seoul.SeoulHospitalInfoResponseDto;
import com.example.mypetlife.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "동물병원 API", description = "Swagger 테스트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospitals")
public class HospitalController {
    private final HospitalService hospitalService;

    // 시 도 필터
    @Operation(summary = "동물병원 도시 필터", description = "파라미터로 받은 도시에대한 시 군 구를 조회합니다.")
    @GetMapping("/{city}")
    public List<DistrictResponseDto> hospitalCityFilter(@Parameter(name = "city", description = "서울특별시 or 경기도")
                                                        @PathVariable(value = "city") final String city) {
        return hospitalService.getHospitalCity(city);
    }

    // 서울 ~구 동물병원 객체 반환

    @Operation(summary = "서울시 시 군 구 동물병원 필터", description = "파라미터로 받은 시 군 구에대한 동물병원 정보를 조회합니다.")
    @GetMapping("/seoul/{city}/{district}")
    public List<SeoulHospitalInfoResponseDto> seoulHospitalDistrictFilter(@Parameter(name = "city", description = "서울특별시") @PathVariable final String city,
                                                                          @Parameter(name = "district", description = "xx구") @PathVariable final String district) {
        return hospitalService.getSeoulHospital(city, district);
    }

    // 경기도 ~시 동물병원 객체 반환 ~구 필터
    @Operation(summary = "경기도 시 군 동물병원 필터", description = "파라미터로 받은 시 군에대한 동물병원 정보와 구, 도로명을 반환합니다.")
    @GetMapping("/gyeonggi/{city}/{country}")
    public List<GyeonggiHospitalCountryResponseDto> gyeonggiDoHospitalCountryFilter(@Parameter(name = "city", description = "경기도") @PathVariable final String city,
                                                                                    @Parameter(name = "country", description = "xx시 or xx군") @PathVariable final String country) {
        return hospitalService.getGyeonggiDoHospitalDistrict(city, country);
    }

    // 경기도 ~시 ~구 동물병원 객체 반환
    @Operation(summary = "경기도 ~시 구 동물병원 필터", description = "파라미터로 받은 구에 대한 동물병원 정보를 반환합니다.")
    @GetMapping("/gyeonggi/{city}/{country}/{district}")
    public List<GyeonggiHospitalInfoResponseDto> gyeonggiHospitalStreetFilter(@Parameter(name = "city", description = "경기도") @PathVariable final String city,
                                                                              @Parameter(name = "country", description = "xx시 or xx군") @PathVariable final String country,
                                                                              @Parameter(name = "district", description = "xx구 or 도로명") @PathVariable final String district) {
        return hospitalService.getGyeonggiDoHospital(city, country, district);
    }
}
