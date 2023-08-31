package com.example.mypetlife.jwt;

import com.example.mypetlife.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtFilter 실행 중에 발생하는 예외를 처리한다.
 */
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("JwtExceptionFilter 실행");
        try {
            filterChain.doFilter(request, response);
            log.info("JwtExceptionFilter 끝");

        } catch (JwtException e) {
            createJwtErrorResponse(request, response, e);
        }
    }

    private void createJwtErrorResponse(HttpServletRequest request, HttpServletResponse response, JwtException e) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("httpStatus", HttpServletResponse.SC_UNAUTHORIZED);
        errorResponse.put("errorName", "JwtException");
        errorResponse.put("message", e.getMessage());

        String responseBody = objectMapper.writeValueAsString(errorResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(responseBody);
    }
}
