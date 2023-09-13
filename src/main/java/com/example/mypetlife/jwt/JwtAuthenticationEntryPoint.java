package com.example.mypetlife.jwt;

import io.jsonwebtoken.JwtException;
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

/*
 * 모든 필터를 처리한 후에 Security Context에 인증 정보가 없으면 AuthenticationException이 발생하고, 이 예외를 처리해주는 핸들러
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        // HandlerExceptionResolver에게 예외 처리를 위임
        // 이때 AuthenticatonException이 아닌 request에 담긴 JwtException을 전달
        resolver.resolveException(request, response, null, (JwtException) request.getAttribute("exception"));
    }
}
