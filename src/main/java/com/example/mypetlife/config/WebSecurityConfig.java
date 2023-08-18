package com.example.mypetlife.config;

import com.example.mypetlife.oauth.OAuth2SuccessHandler;
import com.example.mypetlife.oauth.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final OAuth2UserServiceImpl oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authHttp ->
                        authHttp.requestMatchers("/", "/register", "/login/**", "/views/**", "/token/**").permitAll())
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/views/login")
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo // oauth2 로그인 성공 후 가져올 때의 설정들
                                .userService(oAuth2UserService))) // 소셜로그인 성공 후 후속 조치를 진행할 userService                     
          .sessionManagement(sessionManagment -> sessionManagment
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}