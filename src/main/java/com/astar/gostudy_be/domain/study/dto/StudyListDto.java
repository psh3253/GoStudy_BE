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

    private String filename;

    private String categoryName;

    private StudyType type;

    private String location;

    private Integer currentNumber;

    private Integer recruitmentNumber;

    private JoinType joinType;

    private String introduce;

    public StudyListDto(Study study) {
        this.id = study.getId();
        this.name = study.getName();
        this.filename = study.getImage();
        this.categoryName = study.getCategory().getName();
        this.type = study.getType();
        this.location = study.getLocation();
        this.currentNumber = study.getCurrentNumber();
        this.recruitmentNumber = study.getRecruitmentNumber();
        this.joinType = study.getJoinType();
        this.introduce = study.getIntroduce();
    }
}
