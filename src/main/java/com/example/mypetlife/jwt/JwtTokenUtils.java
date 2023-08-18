package com.example.mypetlife.jwt;

import com.example.mypetlife.entity.CustomUserDetails;
import com.example.mypetlife.service.JpaUserDetailManager;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtils {
    private final Key signingKey;
    private final JwtParser jwtParser; // jwt 해석시 사용되는 Parser
    private final JpaUserDetailManager manager;

    public JwtTokenUtils(@Value("${jwt.secret}") String jwtSecret, JpaUserDetailManager manager) {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build();
        this.manager = manager;
    }

    // 사용자 정보를 바탕으로 JWT를 문자열로 생성
    public String generateToken(CustomUserDetails customUserDetails){
        Claims jwtClaims = Jwts.claims()
                .setSubject(customUserDetails.getEmail()) // JWT에 email 정보 저장
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(600)));

        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(signingKey)
                .compact();
    }

    /*
     * 헤더에서 JWT 가져오기
     */
    public String getTokenFromHeader(HttpServletRequest request) {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header != null && header.startsWith("Bearer ")) {
            return header.split(" ")[1];
        } else {
            log.info("요청 헤더에 토큰이 없음");
            throw new JwtException("요청 헤더에 토큰이 없습니다");
        }
    }

    /*
     * JWT 유효성 검사
     */
    public boolean validateToken(String token) {

        // jjwt 라이브러리는 jwt 해석시 유효하지 않은 jwt이면 예외를 발생시킨다
        try {
            jwtParser.parseClaimsJws(token); // jwt 해석
            return true;
        } catch (MalformedJwtException e) {
            log.info("유효하지 않은 토큰");
            throw new JwtException("유효하지 않은 토큰입니다");
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰");
            throw new JwtException("만료된 토큰입니다");
        } catch (io.jsonwebtoken.JwtException e) {
            log.info("잘못된 토큰");
            throw new JwtException("잘못된 토큰입니다");
        }
    }

    /*
     * JWT -> Authentication
     */
    public Authentication getAuthentication(String token) {

        String email = jwtParser.parseClaimsJws(token).getBody().getSubject();
        UserDetails userDetails = manager.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        return authenticationToken;
    }

    /*
     * request -> token -> 사용자 정보(email) 획득
     */
    public String getEmailFromHeader(HttpServletRequest request) {

        String token = getTokenFromHeader(request);
        String email = jwtParser.parseClaimsJws(token).getBody().getSubject();
        return email;
    }
}
