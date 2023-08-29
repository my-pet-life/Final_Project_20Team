package com.example.mypetlife.controller.view;

import com.example.mypetlife.dto.user.LoginRequestDto;
import com.example.mypetlife.dto.user.RegisterRequest;
import com.example.mypetlife.dto.user.RegisterResponseDto;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.jwt.AccessTokenDto;
import com.example.mypetlife.jwt.JwtTokenDto;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.jwt.RefreshTokenDto;
import com.example.mypetlife.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserViewController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;


    /**
     * [GET] /login
     * 로그인 폼
     */
    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginRequestDto loginRequestDto) {

        return "loginForm";
    }

    /**
     * [POST] /login
     * 일반 회원 로그인: JWT 토큰 발급
     */
    @PostMapping("/login")
    public String login(@ModelAttribute @Validated LoginRequestDto loginRequestDto,
                        BindingResult bindingResult) {

        //아이디, 패스워드가 공백일 경우 오류
        if(bindingResult.hasErrors()) {
            return "loginForm";
        }

        // 로그인 처리: jwt 토큰 발급
        JwtTokenDto token = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        return "redirect:/main?access_token=" + token.getAccessToken() + "refresh_token=" + token.getRefreshToken();
    }

}
