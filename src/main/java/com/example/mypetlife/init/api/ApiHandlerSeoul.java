package com.example.mypetlife.init.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiHandlerSeoul {
    public static StringBuilder openApiInfo(int i) throws IOException {
        // URL + 인증키 + 필수 인자
        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088/4f4547664d746c73373751614f7046/json/LOCALDATA_020301" + "/" + i + "/" + i);
        // 인증키
        urlBuilder.append("/" + URLEncoder.encode("4f4547664d746c73373751614f7046", "UTF-8"));
        // 데이터 파일 타입 (xml,xmlf,xls,json)
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));
        // 서비스명
        urlBuilder.append("/" + URLEncoder.encode("petHospital", "UTF-8"));
        // 요청 시작 위치 (페이지)
        urlBuilder.append("/" + URLEncoder.encode(String.valueOf(i), "UTF-8"));
        // 상위 5개는 필수적으로 순서바꾸지 않고 호출해야 합니다. (형식)

        urlBuilder.append("/" + URLEncoder.encode("20220301", "UTF-8")); /* 서비스별 추가 요청인자들*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode()); // 정상적으로 API가 사용되는지 상태코드 출력
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
        return sb;
    }
}
