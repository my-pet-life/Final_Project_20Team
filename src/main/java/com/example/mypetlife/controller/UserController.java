package com.example.mypetlife.controller;

import com.example.mypetlife.dto.user.LoginRequest;
import com.example.mypetlife.dto.user.RegisterRequest;
import com.example.mypetlife.dto.user.RegisterResponse;
import com.example.mypetlife.entity.user.Authority;
import com.example.mypetlife.entity.user.PetSpecies;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.jwt.AccessTokenDto;
import com.example.mypetlife.jwt.JwtTokenDto;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.jwt.RefreshTokenDto;
import com.example.mypetlife.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * [POST] /register
     * 회원가입
     */
    @PostMapping("/register")
    public RegisterResponse register(@RequestBody @Validated RegisterRequest dto) {

        // 회원 생성
        User user = User.createUser(dto.getUsername(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()),
                                    dto.getPhone(), dto.getBirthDate(), PetSpecies.valueOf(dto.getPetSpices().toUpperCase()),
                                    Authority.ROLE_USER);

        // 회원가입
        Long id = userService.register(user);

        User savedUser = userService.findById(id);

        return new RegisterResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
                savedUser.getPetSpecies(), savedUser.getCreatedAt());
    }

    /**
     * [POST] /login
     * 일반 회원 로그인: JWT 토큰 발급
     */
    @PostMapping("/login")
    public JwtTokenDto login(@RequestBody @Validated LoginRequest dto) {

        JwtTokenDto token = userService.login(dto.getEmail(), dto.getPassword());

        return token;
    }

    /**
     * [GET] /login/kakao
     * 카카오 회원 로그인: JWT 토큰 발급
     */
    @GetMapping("/login/kakao")
    public JwtTokenDto login(@RequestParam(name = "access_token") String accessToken,
                             @RequestParam(name = "refresh_token") String refreshToken) {

        return new JwtTokenDto(accessToken, refreshToken);
    }

    /**
     * [POST] /access_token
     * Refresh Token 검증 후 유효하면 새로운 Access Token 발급, 유효하지 않으면 재로그인하도록
     */
    public AccessTokenDto recreationAccessToke(@RequestBody RefreshTokenDto refreshTokenDto) {

        String refreshToken = refreshTokenDto.getRefreshToken();

        // Refresh Token 유효성 검증 후 유효하면 새로운 Access Token 발급
        try {
            String accessToken = jwtTokenUtils.validateRefreshToken(refreshToken);
            return new AccessTokenDto(accessToken);
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 refresh token입니다.");
        }
    }
}
