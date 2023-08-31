package com.example.mypetlife.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        log.info("JwtAuthenticationEntryPoint 실행");

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("httpStatus", HttpServletResponse.SC_UNAUTHORIZED);
        errorResponse.put("errorName", "AuthenticationException");
        errorResponse.put("message", e.getMessage() + ": 인증되지 않은 사용자입니다.");

        String responseBody = objectMapper.writeValueAsString(errorResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(responseBody);
    }
}
