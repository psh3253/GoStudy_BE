package com.astar.gostudy_be.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class AccountDto {
    private String nickname;

    private String email;

    private String image;

    private String introduce;

    private List<String> roles;

    private String refreshToken;

    @Builder
    public AccountDto(String nickname, String email, String image, String introduce, Long categoryId, List<String> roles, String refreshToken) {
        this.nickname = nickname;
        this.email = email;
        this.image = image;
        this.introduce = introduce;
        this.roles = roles;
        this.refreshToken = refreshToken;
    }
}
