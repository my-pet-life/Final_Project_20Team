package com.example.mypetlife.service;

import com.example.mypetlife.dto.hospital.DistrictResponseDto;
import com.example.mypetlife.dto.hospital.gyeonggiDo.GyeonggiHospitalCountryResponseDto;
import com.example.mypetlife.dto.hospital.gyeonggiDo.GyeonggiHospitalInfoResponseDto;
import com.example.mypetlife.dto.hospital.seoul.SeoulHospitalInfoResponseDto;
import com.example.mypetlife.entity.hospital.GyeonggiDoHospital;
import com.example.mypetlife.entity.hospital.SeoulHospital;
import com.example.mypetlife.repository.hospital.GyeonggiDoHospitalRepository;
import com.example.mypetlife.repository.hospital.SeoulHospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalService {
    private final GyeonggiDoHospitalRepository gyeonggiDoHospitalRepository;
    private final SeoulHospitalRepository seoulHospitalRepository;


    // 1차 필터 서울 / 경기도
    public List<DistrictResponseDto> getHospitalCity(final String city) {
        Set<String> set = new HashSet<>();
        if (city.equals("서울특별시")) {
            List<SeoulHospital> seoulHospitalsCityList = seoulHospitalRepository.findBySeoulAddress_City(city);

            for (int i = 1; i < seoulHospitalsCityList.size(); i++) {
                String district = seoulHospitalsCityList.get(i).getSeoulAddress().getDistrict();
                set.add(district);
            }

        } else if (city.equals("경기도")) {
            List<GyeonggiDoHospital> gyeonggiDoHospitalsCountryList
                    = gyeonggiDoHospitalRepository.findByGyeonggiDoAddress_City(city);

            for (int i = 1; i < gyeonggiDoHospitalsCountryList.size(); i++) {
                String country = gyeonggiDoHospitalsCountryList.get(i).getGyeonggiDoAddress().getCountry();
                set.add(country);
            }
        }


        List<String> cityList = new ArrayList<>(set);
        DistrictResponseDto districtResponseDto = new DistrictResponseDto(city, cityList);

        return List.of(districtResponseDto);
    }

    // 2차 필터 서울시 ~구
    public List<SeoulHospitalInfoResponseDto> getSeoulHospital(final String city, final String district) {
        List<SeoulHospital> seoulHospitalsList
                = seoulHospitalRepository.findBySeoulAddress_CityAndSeoulAddress_District(city, district);
        List<SeoulHospitalInfoResponseDto> seoulHospitalInfoResponseDto = new ArrayList<>();
        for (int i = 1; i < seoulHospitalsList.size(); i++) {
            String findCity = seoulHospitalsList.get(i).getSeoulAddress().getCity();
            String findDistrict = seoulHospitalsList.get(i).getSeoulAddress().getDistrict();
            String findStreet = seoulHospitalsList.get(i).getSeoulAddress().getStreet();
            String findHospitalStreetNumber = seoulHospitalsList.get(i).getSeoulAddress().getHospitalStreetNumber();
            String hospitalTel = seoulHospitalsList.get(i).getHospitalTel();
            int reviewCount = seoulHospitalsList.get(i).getReviews().size();
            String findHospitalName = seoulHospitalsList.get(i).getHospitalName();

            seoulHospitalInfoResponseDto.add(new SeoulHospitalInfoResponseDto(findCity, findDistrict, findStreet,
                    findHospitalStreetNumber, hospitalTel, reviewCount, findHospitalName));
        }

        return seoulHospitalInfoResponseDto;
    }


    // 2차 필터 경기도 xx시의 동물병원 리스트 ~구 필터
    public List<GyeonggiHospitalCountryResponseDto> getGyeonggiDoHospitalDistrict(final String city, final String country) {
        List<GyeonggiDoHospital> gyeonggiDoHospitalsCountryList
                = gyeonggiDoHospitalRepository.findByGyeonggiDoAddress_CityAndGyeonggiDoAddress_Country(city, country);
        Set<String> set = new HashSet<>();
        List<GyeonggiHospitalInfoResponseDto> gyeonggiHospitalInfoResponseDto = new ArrayList<>();


        for (int i = 1; i < gyeonggiDoHospitalsCountryList.size(); i++) {
            String findCity = gyeonggiDoHospitalsCountryList.get(i).getGyeonggiDoAddress().getCity();
            String findCountry = gyeonggiDoHospitalsCountryList.get(i).getGyeonggiDoAddress().getCountry();
            String findDistrict = gyeonggiDoHospitalsCountryList.get(i).getGyeonggiDoAddress().getDistrict();
            String findStreet = gyeonggiDoHospitalsCountryList.get(i).getGyeonggiDoAddress().getStreet();
            String findHospitalStreetNumber = gyeonggiDoHospitalsCountryList.get(i).getGyeonggiDoAddress().getHospitalStreetNumber();
            int reviewCount = gyeonggiDoHospitalsCountryList.get(i).getReviews().size();
            String findHospitalName = gyeonggiDoHospitalsCountryList.get(i).getHospitalName();

            gyeonggiHospitalInfoResponseDto.add(new GyeonggiHospitalInfoResponseDto(findCity, findCountry, findDistrict, findStreet,
                    findHospitalStreetNumber, reviewCount, findHospitalName));

            set.add(findDistrict);
        }

        List<String> list = new ArrayList<>(set);
        GyeonggiHospitalCountryResponseDto gyeonggiHospitalCountryResponseDto = new GyeonggiHospitalCountryResponseDto(gyeonggiHospitalInfoResponseDto, list);
        return List.of(gyeonggiHospitalCountryResponseDto);
    }


    // 3차 필터 경기도 xx시 xx구의 동물병원 리스트
    public List<GyeonggiHospitalInfoResponseDto> getGyeonggiDoHospital(final String city, final String country,
                                                                       final String district) {
        List<GyeonggiDoHospital> gyeonggiDoHospitalsList
                = gyeonggiDoHospitalRepository.
                findByGyeonggiDoAddress_CityAndGyeonggiDoAddress_CountryAndAndGyeonggiDoAddress_District(city, country, district);

        List<GyeonggiHospitalInfoResponseDto> gyeonggiHospitalInfoResponseDto = new ArrayList<>();

        for (int i = 1; i < gyeonggiDoHospitalsList.size(); i++) {
            String findCity = gyeonggiDoHospitalsList.get(i).getGyeonggiDoAddress().getCity();
            String findCountry = gyeonggiDoHospitalsList.get(i).getGyeonggiDoAddress().getCountry();
            String findDistrict = gyeonggiDoHospitalsList.get(i).getGyeonggiDoAddress().getDistrict();
            String findStreet = gyeonggiDoHospitalsList.get(i).getGyeonggiDoAddress().getStreet();
            String findHospitalStreetNumber = gyeonggiDoHospitalsList.get(i).getGyeonggiDoAddress().getHospitalStreetNumber();
            int reviewCount = gyeonggiDoHospitalsList.get(i).getReviews().size();
            String findHospitalName = gyeonggiDoHospitalsList.get(i).getHospitalName();


            gyeonggiHospitalInfoResponseDto.add(new GyeonggiHospitalInfoResponseDto(findCity, findCountry, findDistrict, findStreet,
                    findHospitalStreetNumber, reviewCount, findHospitalName));
        }

        return gyeonggiHospitalInfoResponseDto;
    }
}
