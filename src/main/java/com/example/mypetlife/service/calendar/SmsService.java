package com.example.mypetlife.service.calendar;

import com.example.mypetlife.dto.calendar.alarm.MessageDto;
import com.example.mypetlife.dto.calendar.alarm.SmsRequestDto;
import com.example.mypetlife.dto.calendar.alarm.SmsResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor @Slf4j
public class SmsService {
    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    private String phone;

    // TODO 예약 메세지 전송
    public SmsResponseDto sendSms(MessageDto messageDto, String reserveTime)
            throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignatureOfCreate(time));

        List<MessageDto> messages = new ArrayList<>();
        messages.add(messageDto);

        SmsRequestDto request = SmsRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from("01056644571")
                .content(messageDto.getContent())
                .messages(messages)
                .reserveTime(reserveTime)
                .reserveTimeZone("Asia/Seoul")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        SmsResponseDto response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages"), httpBody, SmsResponseDto.class);

        return response;
    }

    // TODO 예약 메세지 삭제
    public void deleteSms(String reserveId)
            throws RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException{
        Long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignatureOfDelete(time, reserveId));

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        URI canecelUri = new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/reservations/" + reserveId);
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        ResponseEntity<String> response = restTemplate.exchange(canecelUri, HttpMethod.DELETE, httpEntity, String.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            System.out.println("예약된 메시지가 성공적으로 취소되었습니다.");
        } else {
            System.err.println("예약된 메시지 취소에 실패하였습니다. HTTP 상태 코드: " + response.getStatusCode());
        }
    }

    // TODO 메세지 상태 조회
    public ResponseEntity<String> getReserveMessages(String reserveId)
            throws URISyntaxException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        Long time = System.currentTimeMillis();

        // API 엔드포인트 URL 설정
        String apiBaseUrl = "https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/reservations/" + reserveId + "/reserve-status";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("x-ncp-apigw-timestamp", String.valueOf(System.currentTimeMillis()));
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignatureOfSearch(time, reserveId));

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // REST 호출
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(apiBaseUrl);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        return response;
    }

    // TODO 메세지 상태 조회 시, 시그니처 생성 로직 메소드
    public String makeSignatureOfSearch(Long time, String reserveId) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String space = " ";
        String newLine = "\n";
        String method = "GET"; // 또는 API 요청에 맞는 HTTP 메서드를 사용
        String url = "/sms/v2/services/" + this.serviceId + "/reservations/" + reserveId + "/reserve-status"; // API 엔드포인트 URL
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        log.info(message);

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    // TODO 메세지 등록 시, 시그니처 생성 로직 메소드
    public String makeSignatureOfCreate(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/"+ this.serviceId+"/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        log.info(message);

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    // TODO 메세지 삭제 시, 시그니처 생성 로직 메소드
    public String makeSignatureOfDelete(Long time, String reserveId) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "DELETE";
        String url = "/sms/v2/services/"+ this.serviceId + "/reservations/" + reserveId;
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        log.info(message);

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }
}
