package com.astar.gostudy_be.domain.study.dto;

import com.astar.gostudy_be.domain.study.entity.Applicant;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApplicantListDto {
    private Long id;

    private String message;

    private String email;

    private String nickname;

    private String image;

    @JsonProperty("study_id")
    private Long studyId;

    public ApplicantListDto(Applicant applicant) {
        this.id = applicant.getId();
        this.message = applicant.getMessage();
        this.email = applicant.getAccount().getEmail();
        this.nickname = applicant.getAccount().getNickname();
        this.studyId = applicant.getStudy().getId();
    }
}
