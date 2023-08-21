package com.example.mypetlife.init.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHandlerGyeonggiDo {
    public static StringBuilder openApiGyeonggiDo(int i) throws IOException {
        // URL + 인증키 + 필수 인자
        StringBuilder urlBuilder = new StringBuilder("https://openapi.gg.go.kr/Animalhosptl?KEY=26063eb630e744e5be4ee6220db12fdb&Type=json&" + "pIndex=" + i + "&pSize=" + 1);

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
