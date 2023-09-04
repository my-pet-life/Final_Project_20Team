package com.example.mypetlife.service;

import com.example.mypetlife.entity.user.Authority;
import com.example.mypetlife.entity.user.PetSpecies;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.jwt.JwtTokenDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void 회원가입() {

        //given
        User user = User.createUser("kim", "kim@naver.com", passwordEncoder.encode("1111"),
                "01012341234", "20220102", PetSpecies.DOG, Authority.ROLE_USER);

        //when
        userService.register(user);

        //then
        User findUser = userService.findByEmail("kim@naver.com");
        assertEquals(user, findUser);
    }

    @Test
    void 로그인_성공() {

        //given
        User user = User.createUser("kim", "kim@naver.com", passwordEncoder.encode("1111"),
                "01012341234", "20220102", PetSpecies.DOG, Authority.ROLE_USER);

        userService.register(user);

        //when
        JwtTokenDto jwtTokenDto = userService.login("kim@naver.com", "1111");

        //then
        assertThat(jwtTokenDto).isNotNull();
    }

    @Test
    void 로그인_실패_이메일_오류() {

        //given
        User user = User.createUser("kim", "kim@naver.com", passwordEncoder.encode("1111"),
                "01012341234", "20220102", PetSpecies.DOG, Authority.ROLE_USER);

        userService.register(user);

        //when, then
        assertThrows(CustomException.class, () -> userService.login("kim@naver.com", "2222"));
    }

    @Test
    void 로그인_실패_패스워드_오류() {

        //given
        User user = User.createUser("kim", "kim@naver.com", passwordEncoder.encode("1111"),
                "01012341234", "20220102", PetSpecies.DOG, Authority.ROLE_USER);

        userService.register(user);

        //when, then
        assertThrows(CustomException.class, () -> userService.login("kimm@naver.com", "1111"));
    }
}