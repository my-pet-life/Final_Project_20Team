package com.example.mypetlife.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        log.info("JwtAuthenticationEntryPoint 실행");

        // HandlerExceptionResolver에게 예외 처리를 위임
        resolver.resolveException(request, response, null, e);

//        ObjectMapper objectMapper = new ObjectMapper();
//
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("httpStatus", HttpServletResponse.SC_UNAUTHORIZED);
//        errorResponse.put("errorName", "AuthenticationException");
//        errorResponse.put("message", e.getMessage() + ": 인증되지 않은 사용자입니다.");
//
//        String responseBody = objectMapper.writeValueAsString(errorResponse);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("UTF-8");
//        response.setStatus(HttpStatus.FORBIDDEN.value());
//        response.getWriter().write(responseBody);
    }
}
