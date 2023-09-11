package com.example.mypetlife.init.parseApi;

import com.example.mypetlife.init.api.ApiHandlerGyeonggiDo;
import com.example.mypetlife.entity.hospital.address.GyeonggiDoAddress;
import com.example.mypetlife.entity.hospital.GyeonggiDoHospital;
import com.example.mypetlife.repository.hospital.GyeonggiDoHospitalRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParseJsonGyeonggiDo {
    private final GyeonggiDoHospitalRepository gyeonggiDoHospitalRepository;

    @PostConstruct
    public String storeGyeonggiPetHospital() throws IOException, ParseException {
        List<GyeonggiDoHospital> addressList = new ArrayList<>();
        //2311
        for (int i = 1; i <= 2311; i++) {
            StringBuilder sb = ApiHandlerGyeonggiDo.openApiGyeonggiDo(i);

            String jsonResponse = sb.toString();

            // JSON 파싱
            JSONParser parser = new JSONParser();
            // json 코드가 {} 로 감싸고 있을경우
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse.toString());

            // 원하는 정보 추출
            // json 키 값에 접근
            JSONArray animalHosptlList = (JSONArray) jsonObject.get("Animalhosptl");

            // 키값에 접근후, 필요한 index를 사용합니다.
            Object tempObject = animalHosptlList.get(1);

            JSONObject animalHosptl = (JSONObject) tempObject;
            JSONArray petHospitalList = (JSONArray) animalHosptl.get("row");

            for (Object petHospitalObj : petHospitalList) {
                JSONObject petHospitalData = (JSONObject) petHospitalObj;

                String hospitalName = (String) petHospitalData.get("BIZPLC_NM");
                String hospitalAddress = (String) petHospitalData.get("REFINE_ROADNM_ADDR");
                String hospitalTemporaryAddress = (String) petHospitalData.get("REFINE_LOTNO_ADDR");
                String hospitalOperation = (String) petHospitalData.get("BSN_STATE_NM");

                GyeonggiDoHospital gyeonggiDoHospital;

                if (hospitalOperation.contains("운영")) {
                    if (hospitalAddress == null) {
                        String[] hospitalTemporaryAddressList = hospitalTemporaryAddress.split(" ");
                        String city = hospitalTemporaryAddressList[0];
                        String country = hospitalTemporaryAddressList[1];
                        String district = hospitalTemporaryAddressList[2];
                        String street = hospitalTemporaryAddressList[3];


                        if (hospitalTemporaryAddressList.length >= 5) {
                            String streetNumber = hospitalTemporaryAddressList[4];

                            gyeonggiDoHospital = GyeonggiDoHospital.saveHospitalInfo(hospitalName,
                                    new GyeonggiDoAddress(city, country, district, street, streetNumber), hospitalOperation);
                        } else {
                            gyeonggiDoHospital = GyeonggiDoHospital.saveHospitalInfo(hospitalName,
                                    new GyeonggiDoAddress(city, country, district, street, null), hospitalOperation);
                        }
                    } else {
                        String[] hospitalAddressList = hospitalAddress.split(" ");

                        String city = hospitalAddressList[0];
                        String country = hospitalAddressList[1];
                        String district = hospitalAddressList[2];
                        String street = hospitalAddressList[3];
                        if (hospitalAddressList.length >= 5) {
                            String streetNumber = hospitalAddressList[4];

                            gyeonggiDoHospital = GyeonggiDoHospital.saveHospitalInfo(hospitalName,
                                    new GyeonggiDoAddress(city, country, district, street, streetNumber), hospitalOperation);
                        } else {
                            gyeonggiDoHospital = GyeonggiDoHospital.saveHospitalInfo(hospitalName,
                                    new GyeonggiDoAddress(city, country, district, street, null), hospitalOperation);
                        }
                    }
                    addressList.add(gyeonggiDoHospital);
                }
            }
        }
        gyeonggiDoHospitalRepository.saveAll(addressList);
        return null;
    }
}
