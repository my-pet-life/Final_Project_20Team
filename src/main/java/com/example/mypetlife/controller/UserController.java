package com.example.mypetlife.controller;

import com.example.mypetlife.dto.user.LoginRequestDto;
import com.example.mypetlife.dto.user.RegisterRequest;
import com.example.mypetlife.dto.user.RegisterResponseDto;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.jwt.JwtTokenDto;
import com.example.mypetlife.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * [POST] /register
     * 회원가입
     */
    @PostMapping("/register")
    public RegisterResponseDto register(@RequestBody @Validated RegisterRequest dto) {

        // 회원 생성
        User user = User.createUser(dto.getUsername(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()),
                                    dto.getPhone(), dto.getBirthDate(), dto.getPetSpices());

        // 회원가입
        Long id = userService.register(user);
        log.info("id={}", id);

        User savedUser = userService.findById(id);

        return new RegisterResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
                savedUser.getPetSpecies(), savedUser.getCreatedAt());
    }

    /**
     * [POST] /login
     * 일반 회원 로그인: JWT 토큰 발급
     */
    @PostMapping("/login")
    public JwtTokenDto login(@RequestBody @Validated LoginRequestDto dto) {

        JwtTokenDto token = userService.login(dto.getEmail(), dto.getPassword());

        return token;
    }

    /**
     * [GET] /login/kakao
     * 카카오 회원 로그인: JWT 토큰 발급
     */
    @GetMapping("/login/kakao")
    public JwtTokenDto login(@RequestParam String token) {

        JwtTokenDto tokenDto = new JwtTokenDto(token);
        return tokenDto;
    }
}
