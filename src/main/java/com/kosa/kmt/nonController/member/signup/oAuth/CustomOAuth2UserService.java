package com.kosa.kmt.nonController.member.signup.oAuth;

import com.kosa.kmt.nonController.member.Member;
import com.kosa.kmt.nonController.member.MemberRepository;
import com.kosa.kmt.nonController.member.MemberService;
import com.kosa.kmt.nonController.member.signup.MemberDTO;
import com.kosa.kmt.nonController.member.signup.UserRole;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private MemberRepository memberRepository;
    private final HttpSession httpSession;
    @Qualifier("memberService")
    @Autowired
    private MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();


        OAuth2User oAuth2User = null;
        try {
            oAuth2User = delegate.loadUser(userRequest);
            log.info(oAuth2User);
        } catch (OAuth2AuthenticationException e) {
            log.error("Error loading user: {}", e.getMessage(), e);
            throw e;
        }

        // 현재 로그인 진행 중인 서비스를 구분하는 코드 (네이버 로그인인지 구글 로그인인지 구분)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 진행 시 키가 되는 필드 값 (Primary Key와 같은 의미)을 의미
        // 구글의 기본 코드는 "sub", 후에 네이버 로그인과 구글 로그인을 동시 지원할 때 사용
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService를 통해 가져온 OAuthUser의 attribute를 담을 클래스 ( 네이버 등 다른 소셜 로그인도 이 클래스 사용)
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        System.out.println(oAuth2User.getAttributes());
        Member userEntity = saveOrUpdate(attributes);

        if (userEntity == null) {
            log.error("User not found in the database");
            throw new OAuth2AuthenticationException("User not found in the database");
        }

        httpSession.setAttribute("user", new MemberDTO(userEntity));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));

        System.out.println(attributes.getAttributes());
        Map<String, Object> userAttributes = attributes.getAttributes();
        if (userAttributes == null || userAttributes.isEmpty()) {
            log.error("User attributes are empty");
            throw new OAuth2AuthenticationException("User attributes are empty");
        }

        DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User(
                authorities,
                userAttributes,
                attributes.getNameAttributeKey());
        return defaultOAuth2User;
    }
    // 소셜 사용자 정보 업데이트 시 UserEntity 엔티티에 반영
    private Member saveOrUpdate(OAuthAttributes attributes) {
        log.info("Attempting to find member with name: {}", attributes.getName());
        Optional<Member> optionalMember = memberRepository.findByName(attributes.getName());

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            log.info("Member found: {}", member);

            if (member.getAuthEmail() == null) {
                log.info("Updating authEmail for member: {}", attributes.getEmail());
                memberRepository.updateAuthEmail(member, attributes.getEmail());
                System.out.println(memberRepository.findByName(attributes.getName()).get().getAuthEmail());
                log.info("AuthEmail updated : " + memberService.updateAuthEmail(member, attributes.getEmail()));
                return member;
            } else if (member.getAuthEmail().equals(attributes.getEmail())) {
                log.info("Auth email matches for member: {}", member);
                return member;
            } else {
                log.info("Auth email does not match for member: {}", member);
            }
        } else {
            log.info("Member not found with name: {}", attributes.getName());
        }

        // If no member is found or auth email doesn't match, return null
        return null;
    }

}
