package com.kosa.kmt.non_controller.member.signup.oAuth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String provider;
    private String providerId;


    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String provider, String providerId) {
        this.attributes = attributes;

        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;

    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {

            return ofNaver(registrationId, userNameAttributeName, attributes);
        }

        return ofGoogle(registrationId, userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofNaver(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");


        return OAuthAttributes.builder()
                .attributes(attributes)
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .nameAttributeKey(userNameAttributeName)
                .provider(registrationId)
                .build();
    }

    private static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .provider(registrationId)
                .providerId((String) attributes.get("sub"))
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

}