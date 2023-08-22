package com.example.mypetlife.service;

import com.example.mypetlife.entity.CustomUserDetails;
import com.example.mypetlife.entity.user.User;
import com.example.mypetlife.exception.CustomException;
import com.example.mypetlife.exception.ErrorCode;
import com.example.mypetlife.jwt.JwtTokenDto;
import com.example.mypetlife.jwt.JwtTokenUtils;
import com.example.mypetlife.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        if(optionalUser.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
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
        User findUser = findByEmail(email);

        if(findUser == null) {
            throw new CustomException(ErrorCode.WrongEmail);
        }

        // password 검증: 해당 email에 password가 맞는지 확인
        CustomUserDetails user = (CustomUserDetails) manager.loadUserByUsername(email);
        if(!passwordEncoder.matches(password, user.getPassword())){
            log.info("입력된 패스워드:{}", password);
            log.info("저장된 패스워드:{}", user.getPassword());

            throw new CustomException(ErrorCode.WrongPassword);

        }

        // 토큰 발급
        JwtTokenDto response = new JwtTokenDto(jwtTokenUtils.generateToken(user));
        log.info(response.toString());
        return response;
    }

    public User findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return user;
    }

    public User findByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return user;
    }
}
