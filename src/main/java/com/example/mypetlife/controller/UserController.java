package com.example.mypetlife.controller;

import com.example.mypetlife.dto.LoginRequestDto;
import com.example.mypetlife.dto.RegisterRequestDto;
import com.example.mypetlife.entity.User;
import com.example.mypetlife.jwt.JwtTokenDto;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * [POST] /register
     * 회원가입
     */
    @PostMapping("/register")
    public String register(@RequestBody @Validated RegisterRequestDto dto) {

        User user = User.createUser(dto.getUsername(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()),
                                    dto.getPhone(), dto.getBirthDate(), dto.getPetSpices());

        userService.register(user);

        return "success";
    }

    /**
     * [POST] /login
     * 로그인: JWT 토큰 발급
     */
    @PostMapping("/login")
    public JwtTokenDto login(@RequestBody @Validated LoginRequestDto dto) {

        JwtTokenDto token =userService.login(dto.getEmail(), dto.getPassword());

        return token;
    }
}
