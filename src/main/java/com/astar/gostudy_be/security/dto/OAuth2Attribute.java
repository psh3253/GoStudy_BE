package com.astar.gostudy_be.security.dto;

import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String nickname;
    private String image;

    @Builder
    public OAuth2Attribute(Map<String, Object> attributes, String attributeKey, String email, String nickname, String image) {
        this.attributes = attributes;
        this.attributeKey = attributeKey;
        this.email = email;
        this.nickname = nickname;
        this.image = image;
    }

    public static OAuth2Attribute of(String registrationId, String attributeKey, Map<String, Object> attributes) {
        switch (registrationId) {
            case "kakao":
                return ofKakao("id", attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attribute ofKakao(String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .nickname((String)profile.get("nickname"))
                .image((String) profile.get("profile_image_url"))
                .email((String) kakaoAccount.get("email"))
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("nickname", nickname);
        map.put("email", email);
        map.put("image", image);
        return map;
    }

    public Account toEntity() {
        return Account.builder()
                .nickname(nickname)
                .email(email)
                .image(image)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}
