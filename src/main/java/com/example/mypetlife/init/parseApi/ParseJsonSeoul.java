package com.example.mypetlife.init.parseApi;

import com.example.mypetlife.init.api.ApiHandlerSeoul;
import com.example.mypetlife.entity.hospital.SeoulHospital;
import com.example.mypetlife.entity.hospital.address.SeoulAddress;
import com.example.mypetlife.repository.hospital.SeoulHospitalRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ParseJsonSeoul {
    private final SeoulHospitalRepository seoulHospitalRepository;

    //@PostConstruct
    public String storeSeoulPetHospital() throws IOException, ParseException {
        List<SeoulHospital> addressList = new ArrayList<>();
        // 2079
        for (int i = 1; i <= 2079; i++) {
            StringBuilder sb = ApiHandlerSeoul.openApiInfo(i);

            String jsonResponse = sb.toString();

            // JSON 파싱
            JSONParser parser = new JSONParser();
            // json 코드가 {} 로 감싸고 있을경우
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);

            // 원하는 정보 추출
            // json 키 값에 접근
            JSONObject localdata = (JSONObject) jsonObject.get("LOCALDATA_020301");

            // json 코드가 [] 로 감싸고 있을경우
            JSONArray petHospitalList = (JSONArray) localdata.get("row");
            for (Object petHospitalObj : petHospitalList) {
                JSONObject petHospital = (JSONObject) petHospitalObj;
                String hospitalName = (String) petHospital.get("BPLCNM");
                String hospitalAddress = (String) petHospital.get("RDNWHLADDR");
                String hospitalTemporaryAddress = (String) petHospital.get("SITEWHLADDR");
                String hospitalTel = (String) petHospital.get("SITETEL");
                String hospitalOperation = (String) petHospital.get("DTLSTATENM");

                String[] hospitalAddressList = hospitalAddress.split(" ");
                String[] hospitalTemporaryAddressList = hospitalTemporaryAddress.split(" ");


                SeoulHospital seoulHospital = null;
                if (hospitalOperation.equals("정상")) {
                    if (hospitalAddressList.length > 2) {
                        String metropolitanCity = hospitalAddressList[0];
                        String district = hospitalAddressList[1];
                        String street = hospitalAddressList[2];
                        String streetNumber = hospitalAddressList[3];

                        seoulHospital =
                                SeoulHospital.saveHospitalInfo(hospitalName,
                                        new SeoulAddress(metropolitanCity, district, street, streetNumber),
                                        hospitalTel, hospitalOperation);
                    } else {
                        try {
                            String metropolitanCity = hospitalTemporaryAddressList[0];
                            String district = hospitalTemporaryAddressList[1];
                            String street = hospitalTemporaryAddressList[2];
                            String streetNumber = hospitalTemporaryAddressList[3];

                            seoulHospital =
                                    SeoulHospital.saveHospitalInfo(hospitalName,
                                            new SeoulAddress(metropolitanCity, district, street, streetNumber),
                                            hospitalTel, hospitalOperation);
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("유효하지 않은 인덱스 범위입니다.");
                        }
                    }
                    addressList.add(seoulHospital);
                }
            }
        }
        seoulHospitalRepository.saveAll(addressList);
        return null;
    }
}
