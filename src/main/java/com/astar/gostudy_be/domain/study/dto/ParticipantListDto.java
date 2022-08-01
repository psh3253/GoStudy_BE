package com.astar.gostudy_be.domain.study.dto;

import com.astar.gostudy_be.domain.study.entity.Participant;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ParticipantListDto {
    private Long id;

    private String email;

    private String nickname;

    private String image;

    private String introduce;

    public ParticipantListDto(Participant participant) {
        this.id = participant.getId();
        this.email = participant.getAccount().getEmail();
        this.nickname = participant.getAccount().getNickname();
        this.image = participant.getAccount().getImage();
        this.introduce = participant.getAccount().getIntroduce();
    }
}
