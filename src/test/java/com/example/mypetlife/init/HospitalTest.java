package com.example.mypetlife.init;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HospitalTest {
    private static final int START_INDEX = 1;
    private static final int PAGE_SIZE = 100;
    private final List<String> hospitalList = new ArrayList<>();
    private final List<String> hospitalAddressList = new ArrayList<>();
    private String hospitalName = " ";
    private String hospitalOperation = " ";
    private String hospitalAddress = " ";

    @Test
    void 경기도_공공데이터_오픈_API_운영중인_동물병원_테스트() throws IOException, ParseException {
        for (int i = START_INDEX; i <= PAGE_SIZE; i++) {
            JSONArray petHospitalList = getGyeonggiOpenApi(i);

            for (Object petHospitalObj : petHospitalList) {
                JSONObject petHospitalData = (JSONObject) petHospitalObj;

                hospitalName = (String) petHospitalData.get("BIZPLC_NM");
                hospitalOperation = (String) petHospitalData.get("BSN_STATE_NM");
            }

            if (hospitalOperation.contains("운영중")) {
                hospitalList.add(hospitalName);
            }
        }
        assertThat(hospitalList.size()).isNotEqualTo(0);
        assertThat(hospitalList.get(0)).isEqualTo("현대동물병원");
    }

    @Test
    void 경기도_공공데이터_오픈_API_지역_테스트() throws IOException, ParseException {
        for (int i = START_INDEX; i <= PAGE_SIZE; i++) {
            JSONArray petHospitalList = getGyeonggiOpenApi(i);


            for (Object petHospitalObj : petHospitalList) {
                JSONObject petHospitalData = (JSONObject) petHospitalObj;

                hospitalAddress = (String) petHospitalData.get("REFINE_ROADNM_ADDR");
                hospitalOperation = (String) petHospitalData.get("BSN_STATE_NM");
            }

            if (hospitalOperation.contains("운영중")) {
                hospitalAddressList.add(hospitalAddress);
            }
        }
        assertThat(hospitalAddressList.get(0).split(" ")[0]).isEqualTo("경기도");
        assertThat(hospitalAddressList.get(0).split(" ")[0]).isNotEqualTo("서울특별시");
    }

    @Test
    void 서울_공공데이터_오픈_API_운영중인_동물병원_테스트() throws IOException, ParseException {
        for (int i = START_INDEX; i <= PAGE_SIZE; i++) {
            JSONArray petHospitalList = getOpenSeoulApi(i);

            for (Object petHospitalObj : petHospitalList) {
                JSONObject petHospital = (JSONObject) petHospitalObj;
                hospitalName = (String) petHospital.get("BPLCNM");
                hospitalOperation = (String) petHospital.get("DTLSTATENM");
            }

            if (hospitalOperation.contains("정상")) {
                hospitalList.add(hospitalName);
            }
        }
        assertThat(hospitalList.size()).isNotEqualTo(0);
        assertThat(hospitalList.get(0)).isEqualTo("차지우동물병원");
    }

    @Test
    void 서울_공공데이터_오픈_API_지역_테스트() throws IOException, ParseException {
        for (int i = START_INDEX; i <= PAGE_SIZE; i++) {
            JSONArray petHospitalList = getOpenSeoulApi(i);

            for (Object petHospitalObj : petHospitalList) {
                JSONObject petHospital = (JSONObject) petHospitalObj;
                hospitalAddress = (String) petHospital.get("RDNWHLADDR");
                hospitalOperation = (String) petHospital.get("DTLSTATENM");
            }

            if (hospitalOperation.contains("정상")) {
                hospitalAddressList.add(hospitalAddress);
            }
        }
        assertThat(hospitalAddressList.get(0).split(" ")[0]).isEqualTo("서울특별시");
        assertThat(hospitalAddressList.get(0).split(" ")[0]).isNotEqualTo("경기도");
    }

    private static JSONArray getOpenSeoulApi(int i) throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088/4f4547664d746c73373751614f7046/json/LOCALDATA_020301" + "/" + i + "/" + i);
        urlBuilder.append("/" + URLEncoder.encode("4f4547664d746c73373751614f7046", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("petHospital", "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode(String.valueOf(i), "UTF-8"));
        urlBuilder.append("/" + URLEncoder.encode("20220301", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String jsonResponse = sb.toString();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse.toString());
        JSONObject localdata = (JSONObject) jsonObject.get("LOCALDATA_020301");

        JSONArray petHospitalList = (JSONArray) localdata.get("row");
        return petHospitalList;
    }

    private static JSONArray getGyeonggiOpenApi(int i) throws IOException, ParseException {
        URL url = new URL("https://openapi.gg.go.kr/Animalhosptl?KEY=26063eb630e744e5be4ee6220db12fdb&Type=json&" + "pIndex=" + i + "&pSize=" + 1);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String jsonResponse = sb.toString();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse.toString());

        JSONArray animalHosptlList = (JSONArray) jsonObject.get("Animalhosptl");

        Object tempObject = animalHosptlList.get(1);

        JSONObject animalHospital = (JSONObject) tempObject;
        JSONArray petHospitalList = (JSONArray) animalHospital.get("row");
        return petHospitalList;
    }
}
