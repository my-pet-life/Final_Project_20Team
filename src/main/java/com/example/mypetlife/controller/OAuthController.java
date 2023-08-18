package com.example.mypetlife.controller;

import com.example.mypetlife.oauth.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OAuthController {

    private final OAuth2UserServiceImpl oAuth2UserService;

    // 인가 코드가 돌아오는 곳
//    @GetMapping("/login/oauth2/kakao")
//    public void kakaoAuth(@RequestParam String code, RedirectAttributes redirectAttributes) {
//
//        log.info("응답코드={}", code);
//    }

    @GetMapping("/token/val")
    public String getToken(@RequestParam String token) {

        log.info("로그인 성공, 토큰={}", token);
        return token;
    }
}
