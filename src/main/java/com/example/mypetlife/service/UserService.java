package com.example.mypetlife.service;

import com.example.mypetlife.entity.User;
import com.example.mypetlife.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

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
    public String login(String email, String password) {

        // email 검증: 회원가입된 email인지 확인
        User findUser = userRepository.findByEmail(email);

        if(findUser == null) {
            throw new RuntimeException();
        }

        // password 검증: 해당 email에 password가 맞는지 확인
        if(!findUser.getPassword().equals(password)) {
            throw new RuntimeException();
        }

        // 로그인(JWT 토큰 발급)
        return null;
    }
}