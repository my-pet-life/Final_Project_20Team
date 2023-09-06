package com.example.mypetlife.controller.user;

import com.example.mypetlife.dto.community.article.CreateArticleRequest;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@io.swagger.v3.oas.annotations.tags.Tag(name = "회원", description = "회원 API")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * [POST] /register
     * 회원가입
     */
    @PostMapping("/register")
    @Operation(summary = "회원가입")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value =
                            "{\"username\":\"jeon\",\"email\":\"jeon@naver.com\",\"password\":\"1234\", " +
                                    "\"phone\":\"01012341234\", \"birthDate\":\"220103\", \"petSpices\":\"dog\"}"),
                    schema = @Schema(implementation = RegisterRequest.class))
    )
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
    @Operation(summary = "일반 로그인")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value =
                            "{\"email\":\"jeon@naver.com\",\"password\":\"1234\"}"),
                    schema = @Schema(implementation = LoginRequest.class))
    )
    public JwtTokenDto login(@RequestBody @Validated LoginRequest dto) {

        JwtTokenDto token = userService.login(dto.getEmail(), dto.getPassword());

        return token;
    }

    /**
     * [GET] /login/kakao
     * 카카오 회원 로그인: JWT 토큰 발급
     */
    @GetMapping("/login/kakao")
    @Operation(summary = "카카오 로그인")
    public JwtTokenDto login(@RequestParam(name = "access_token") String accessToken,
                             @RequestParam(name = "refresh_token") String refreshToken) {

        return new JwtTokenDto(accessToken, refreshToken);
    }

    /**
     * [POST] /access_token
     * Refresh Token 검증 후 유효하면 새로운 Access Token 발급, 유효하지 않으면 재로그인하도록
     */
    @PostMapping("/access_token")
    @Operation(summary = "access token 재발급")
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
