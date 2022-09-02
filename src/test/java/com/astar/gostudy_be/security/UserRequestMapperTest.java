package com.astar.gostudy_be.security;

import com.astar.gostudy_be.domain.user.dto.AccountDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserRequestMapperTest {

    @InjectMocks
    UserRequestMapper userRequestMapper;

    @Test
    void toDto() {
        // given
        String nickname = "닉네임 1";
        String image = "이미지 1";
        String email = "이메일 1";

        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("nickname", nickname);
        userAttributes.put("image", image);
        userAttributes.put("email", email);
        OAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                userAttributes, "email");

        // when
        AccountDto accountDto = userRequestMapper.toDto(oAuth2User);

        // then
        assertThat(accountDto.getEmail()).isEqualTo(email);
        assertThat(accountDto.getImage()).isEqualTo(image);
        assertThat(accountDto.getNickname()).isEqualTo(nickname);
    }
}