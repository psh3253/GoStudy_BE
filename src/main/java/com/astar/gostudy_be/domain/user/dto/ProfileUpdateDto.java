package com.astar.gostudy_be.domain.user.dto;

import com.astar.gostudy_be.domain.user.entity.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter
public class ProfileUpdateDto {
    private String nickname;

    private Boolean setDefaultImage;

    @JsonIgnore
    private MultipartFile image;

    private String introduce;

    public Account toEntity(String image) {
        return Account.builder()
                .nickname(this.nickname)
                .image(image)
                .introduce(this.introduce)
                .build();
    }
}
