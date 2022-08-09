package com.astar.gostudy_be.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileDto {
    private Long id;
    private String nickname;

    private String email;

    private String image;

    private String introduce;

    public ProfileDto(Long id, String nickname, String email, String image, String introduce) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.image = image;
        this.introduce = introduce;
    }
}
