package com.kosa.kmt.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    //DefaultOAuth2UserService OAuth2UserService의 구현체
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("OAUTH2User: " + oAuth2User);
        log.info("oAuth2User ::::::❤❤❤ ");

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
//        else if (registrationId.equals("google")) {
//            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
//        }
        else {
            return null;
        }
        String username = oAuth2Response.getName();
        Member existData = memberRepository.findByName(username).get();
        log.info("existData ::::::" + existData);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        String email = oAuth2Response.getEmail();
        if (existData != null) {
            if (existData.getAuthEmail() == null) {
                if (existData.getName().equals(username)) {
                    existData.setAuthEmail(email);
                    memberRepository.save(existData);
                    //memberService.updateAuthEmail(existData, email);
                    return new CustomOAuth2User(oAuth2Response, UserRole.USER.getValue());
                }
                throw new OAuth2AuthenticationException("User not found with email: " + email);
            } else {
                if (existData.getAuthEmail().equals(email)) {
                    return new CustomOAuth2User(oAuth2Response, UserRole.USER.getValue());
                }
                throw new OAuth2AuthenticationException("User not found with email: " + email);
            }
        }

        return null;
    }
}