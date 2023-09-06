package com.example.mypetlife.controller.hospitalView;

import com.example.mypetlife.dto.hospital.DistrictResponseDto;
import com.example.mypetlife.dto.hospital.gyeonggiDo.GyeonggiHospitalCountryResponseDto;
import com.example.mypetlife.dto.hospital.seoul.SeoulHospitalInfoResponseDto;
import com.example.mypetlife.service.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hospitals")
@Slf4j
public class HospitalViewController {
    private final HospitalService hospitalService;

    @GetMapping
    public String hospitalCityFilter() {
        return "selectCity";
    }

    // 구 필터
    @GetMapping("/city")
    public String cityFilter(@RequestParam("city") final String selectCity, final Model model) {
        List<DistrictResponseDto> hospitalCity = hospitalService.getHospitalCity(selectCity);
        model.addAttribute("selectCity", hospitalCity);
        log.info("구 : {}", hospitalCity.size());
        return "selectionCityFilter";
    }

    @GetMapping("/city/country")
    public  String hospitalCityFilter(@RequestParam("city") final String selectCity,
                                      @RequestParam("country") final String selectCountry,
                                      final Model model) {

        if(selectCity.equals("서울특별시")){
            List<SeoulHospitalInfoResponseDto> countryList = hospitalService.getSeoulHospital(selectCity, selectCountry);
            model.addAttribute("selectCity", selectCity);
            model.addAttribute("countryList", countryList);
            return "seoulSelectionCountryFilter"; // 뷰 이름 반환
        }else {
            List<GyeonggiHospitalCountryResponseDto> gyeonggiDoHospitalDistrict
                    = hospitalService.getGyeonggiDoHospitalDistrict(selectCity, selectCountry);
            model.addAttribute("selectCity", selectCity);
            model.addAttribute("countryList", gyeonggiDoHospitalDistrict);
            return "gyeonggiSelectionCountryFilter";
        }
    }
}
