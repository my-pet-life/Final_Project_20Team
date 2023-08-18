package com.example.mypetlife.service;

import com.example.mypetlife.entity.User;
import com.example.mypetlife.jwt.JwtTokenDto;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /*
     * 회원가입
     */
    public Long register(User user) {

        // email 검증: 이미 회원가입된 email인지 확인
        User findUser = userRepository.findByEmail(user.getEmail());

        if(findUser != null) {
            throw new RuntimeException();
        }

        // 회원가입
        userRepository.save(user);
        return user.getId();
    }

    /*
     * 로그인: JWT 토큰 반환
     */
    public JwtTokenDto login(String email, String password) {

        // email 검증: 회원가입된 email인지 확인
        User findUser = userRepository.findByEmail(email);

        if(findUser == null) {
            throw new RuntimeException();
        }

        // password 검증: 해당 email에 password가 맞는지 확인
        UserDetails user = manager.loadUserByUsername(email);
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 토큰 발급
        JwtTokenDto response = new JwtTokenDto(jwtTokenUtils.generateToken(user));
        log.info(response.toString());
        return response;
    }
}
