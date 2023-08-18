package com.example.mypetlife.oauth;

import com.example.mypetlife.entity.User;
import com.example.mypetlife.jwt.JwtTokenDto;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.repository.UserRepository;
import com.example.mypetlife.service.JpaUserDetailManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
// OAuth2 통신이 성공적으로 끝났을 때 실행되는 클래스
// SimpleUrlAuthenticationSuccessHandler: 인증 성공 후 특정 url로 리다이렉트 시키고 싶을 때 활용하는 핸들러
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JpaUserDetailManager manager;
    private final JwtTokenUtils jwtTokenUtils;

    // 인증 성공시 호출되는 메소드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("OAuth2SuccessHandler 실행");

        // OAuth2UserServiceImpl에서 반환한 DefaultOAuth2User가 저장된다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // db에 저장된 회원 정보를 토대로 토큰 생성
        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttribute("kakao_account");
        String email = (String)kakaoAccount.get("email");
        // 회원 조회
        UserDetails user = manager.loadUserByUsername(email);

        // 토큰 발급
        String token = jwtTokenUtils.generateToken(user);

        // 파라미터로 토큰을 전달하면서 리다이렉트
        String redirectUrl = String.format("http://localhost:8080/login/kakao?token=%s", token);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}