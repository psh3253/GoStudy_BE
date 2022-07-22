package com.astar.gostudy_be.security;

import com.astar.gostudy_be.domain.user.dto.AccountDto;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserRequestMapper {
    public AccountDto toDto(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return AccountDto.builder()
                .nickname((String)attributes.get("nickname"))
                .email((String)attributes.get("email"))
                .image((String)attributes.get("image"))
                .build();
    }

    //
}
