package com.example.mypetlife.oauth;
import com.example.mypetlife.entity.User;
import com.example.mypetlife.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//    private final UserRepository userRepository;
//    private final HttpSession httpSession;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//
//        log.info("CustomOAuth2UserService.loadUser() 실행");
//
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = delegate.loadUser(userRequest);
//
//        // OAuth2 서비스 id (구글, 카카오, 네이버)
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//
//        // OAuth2 로그인 진행 시 키가 되는 필드 값(PK)
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//
//        // OAuth2UserService
//        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//        User user = saveOrUpdate(attributes);
////        httpSession.setAttribute("user", new SessionUser(user)); // SessionUser (직렬화된 dto 클래스 사용)
//
//        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("USER")),
//                attributes.getAttributes(),
//                attributes.getNameAttributeKey());
//    }
//
//    // 유저 생성 및 수정 서비스 로직
//    private User saveOrUpdate(OAuthAttributes attributes){
//
//        // 회원가입
//        if(userRepository.findByEmail(attributes.getEmail()) == null) {
//            User user = attributes.toEntity();
//            userRepository.save(user);
//            return user;
//        }
//
//        return null;
////        User user = userRepository.findByEmail(attributes.getEmail())
////                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
////                .orElse(attributes.toEntity());
////        return userRepository.save(user);
//    }
//}

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;


    // access token을 얻은 후 실행되는 메소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("OAuth2UserServiceImpl 실행");

        // access token을 이용해서 사용자 정보를 받아오기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth2 서비스 id (구글, 카카오, 네이버) (application.yml에 등록한 Privider id(naver, kakao))
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 진행 시 키가 되는 필드 값(PK)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        User user = saveOrUpdate(attributes);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes){

        // 회원가입
        if(userRepository.findByEmail(attributes.getEmail()) == null) {
            User user = attributes.toEntity();
            userRepository.save(user);
            return user;
        }

        return null;
    }
}