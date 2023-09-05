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
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.socket.WebSocketSession;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtils {
    private final Key signingKey;
    private final JwtParser jwtParser;
    private final JpaUserDetailManager manager;

    public JwtTokenUtils(@Value("${jwt.secret}") String secretKeyPlain, JpaUserDetailManager manager) {
        this.signingKey = Keys.hmacShaKeyFor(secretKeyPlain.getBytes());
        jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build();
        this.manager = manager;
    }

    /*
     * 최초 로그인시 JWT 토큰(Access, Refresh Token) 발급
     */
    public JwtTokenDto generateToken(CustomUserDetails customUserDetails){

        Claims claims = Jwts.claims()
                .setSubject(customUserDetails.getEmail()); // JWT의 payload에 사용자 이메일 저장

        // Access Token
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(60 * 30)))
                .signWith(SignatureAlgorithm.HS256, signingKey) // 사용할 암호화 알고리즘과 secret 값
                .setHeaderParam("type","jwt")
                .compact();

        // Refresh Token
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(60 * 60)))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();

        return new JwtTokenDto(accessToken, refreshToken);
    }

    /*
     * Access Token 유효성 검사
     */
    public boolean validateAccessToken(String accessToken) {

        // jjwt 라이브러리는 jwt 해석시 유효하지 않은 jwt이면 예외를 발생시킨다
        try {
            jwtParser.parseClaimsJws(accessToken); // jwt 해석
            return true;
        } catch (MalformedJwtException e) {
            throw new JwtException("유효하지 않은 토큰입니다");
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 토큰입니다. Refresh Token을 전달해 Access Token을 재발급하세요");
        } catch (io.jsonwebtoken.JwtException e) {
            throw new JwtException("잘못된 토큰입니다");
        }
    }

    /*
     * Refresh Token 유효성 검사
     */
    public String validateRefreshToken(String refreshToken) {

        // jjwt 라이브러리는 jwt 해석시 유효하지 않은 jwt이면 예외를 발생시킨다
        Jws<Claims> claims = jwtParser.parseClaimsJws(refreshToken);

        // 검증 성공하면 새로운 AccessToken 발급
        String accessToken = recreationAccessToken(claims.getBody().getSubject());
        return accessToken;
    }

    /*
     * Access Token 재발급
     */
    public String recreationAccessToken(String email) {

        Claims claims = Jwts.claims()
                .setSubject(email); // JWT의 payload에 사용자 이메일 저장
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(60 * 30)))
                .signWith(SignatureAlgorithm.HS256, signingKey) // 사용할 암호화 알고리즘과 secret 값
                .setHeaderParam("type","jwt")
                .compact();

        return accessToken;
    }

    /*
     * 헤더에서 JWT 가져오기
     */
    public String getTokenFromHeader(HttpServletRequest request) {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header != null && header.startsWith("Bearer ")) {
            return header.split(" ")[1];
        } else {
            throw new JwtException("요청 헤더에 토큰이 없습니다");
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

    // WebSocketSession -> 사용자 정보
    public String getEmailFromSession(WebSocketSession session){
        String token = session.getHandshakeHeaders().get("Authorization").get(0);
        String jwt = "";
        if(token != null && token.startsWith("Bearer ")) {
            jwt= token.split(" ")[1];
        } else {
            log.info("요청 헤더에 토큰이 없음");
            throw new JwtException("요청 헤더에 토큰이 없습니다");
        }
        String email = jwtParser.parseClaimsJws(jwt).getBody().getSubject();
        return email;
    }
}
