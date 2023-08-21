package com.example.mypetlife.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        log.info("JwtFilter 실행");

        // 요청헤더에 토큰이 있는지 검증
        String token = jwtTokenUtils.getTokenFromHeader(request);

        // 있다면, 토큰이 유효한지 검증
        if(jwtTokenUtils.validateToken(token)) {

            // 토큰이 유효하면 authentication 반환 후 SecurityContextHolder에 저장
            Authentication authentication = jwtTokenUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("SecurityContextHolder에 authentication 저장 완료");
        }

        filterChain.doFilter(request, response);

        log.info("JwtFilter 끝");
    }
}
