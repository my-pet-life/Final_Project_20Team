package com.example.mypetlife.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtils {
    private final Key signingKey;

    public JwtTokenUtils(
            @Value("${jwt.secret}") String jwtSecret){
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // 사용자 정보를 바탕으로 JWT를 문자열로 생성
    public String generateToken(UserDetails userDetails){
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(600)));

        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(signingKey)
                .compact();
    }
}
