package com.example.mypetlife.config;

import com.example.mypetlife.jwt.JwtExceptionFilter;
import com.example.mypetlife.jwt.JwtFilter;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final OAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtTokenUtils jwtTokenUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authHttp ->
                        authHttp.requestMatchers("/api/register", "/api/hospitals/**/",
                                        "/","/v3/api-docs/**", "/swagger-ui/**"
                                        , "/register", "/login/**", "/community/search/**", "/hospitals/**", "/access_token").permitAll()
                                .requestMatchers(GET, "/community/articles/**").permitAll()
                                .requestMatchers(GET, "/community").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo // oauth2 로그인 성공 후 가져올 때의 설정들
                                .userService(oAuth2UserService) // 소셜로그인 성공 후 후속 조치를 진행할 userService
                        )
                )
                .sessionManagement(sessionManagment -> sessionManagment
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // JwtFilter를 SecurityChainFilter에 등록하여 특정 URL에 접근하기 전에 로그인 유무를 확인한다.
                .addFilterBefore(new JwtFilter(jwtTokenUtils), AuthorizationFilter.class)
                // JwtExceptionFilter를 SecurityChainFilter에 등록하여 JwtFilter에서 발생한 예외를 처리한다.
                // 즉 특정 URL에 로그인 없이 접근하면 예외가 발생하고, 해당 필터가 예외를 처리한다.
                .addFilterBefore(new JwtExceptionFilter(), JwtFilter.class)
        ;

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring()
                    // 해당 경로는 security filter chain을 생략
                    // 즉 permitAll로 설정하여 로그인 없이 접근 가능한 URL을 아래에 추가하여
                    // 해당 URL 요청들은 JwtFilter, JwtExceptionFilter를 포함한 스프링 시큐리티의 필터 체인을 생략
                    .requestMatchers("/api/register", "/api/hospitals/**/",
                            "/", "/v3/api-docs/**", "/swagger-ui/**",
                            "/register", "/login/**", "/community/search/**", "/hospitals/**", "/access_token")
                    .requestMatchers(GET, "/community/articles/**")
                    .requestMatchers(GET, "/community");
        };

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
