package com.astar.gostudy_be.domain.study.dto;

import com.astar.gostudy_be.domain.study.entity.JoinType;
import com.astar.gostudy_be.domain.study.entity.Study;
import com.astar.gostudy_be.domain.study.entity.StudyType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StudyListDto {
    private Long id;
    private String name;

    private String imagePath;

    private Long categoryId;

    private StudyType type;

    private String location;

    private Integer currentNumber;

    private Integer recruitmentNumber;

    private JoinType joinType;

    public StudyListDto(Study study) {
        this.id = study.getId();
        this.name = study.getName();
        this.imagePath = study.getImage().getPath();
        this.categoryId = study.getCategory().getId();
        this.type = study.getType();
        this.location = study.getLocation();
        this.currentNumber = study.getCurrentNumber();
        this.recruitmentNumber = study.getRecruitmentNumber();
        this.joinType = study.getJoinType();
    }
}
