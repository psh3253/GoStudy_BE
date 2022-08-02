package com.astar.gostudy_be.domain.study.dto;

import com.astar.gostudy_be.domain.study.entity.JoinType;
import com.astar.gostudy_be.domain.study.entity.Study;
import com.astar.gostudy_be.domain.study.entity.StudyType;
import com.astar.gostudy_be.domain.study.entity.Visibility;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StudyDto {
    private Long id;
    private String name;

    private String filename;

    private Long categoryId;

    private String categoryName;

    private StudyType type;

    private String location;

    private Integer currentNumber;

    private Integer recruitmentNumber;

    private Visibility visibility;

    private JoinType joinType;

    private Boolean isRecruiting;

    private String introduce;

    private String creatorEmail;

    public StudyDto(Study study) {
        this.id = study.getId();
        this.name = study.getName();
        this.filename = study.getImage().getFilename();
        this.categoryId = study.getCategory().getId();
        this.categoryName = study.getCategory().getName();
        this.type = study.getType();
        this.location = study.getLocation();
        this.currentNumber = study.getCurrentNumber();
        this.recruitmentNumber = study.getRecruitmentNumber();
        this.visibility = study.getVisibility();
        this.joinType = study.getJoinType();
        this.isRecruiting = study.getIsRecruiting();
        this.introduce = study.getIntroduce();
        this.creatorEmail = study.getAccount().getEmail();
    }
}
