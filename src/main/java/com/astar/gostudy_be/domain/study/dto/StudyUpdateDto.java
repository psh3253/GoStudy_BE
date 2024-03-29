package com.astar.gostudy_be.domain.study.dto;

import com.astar.gostudy_be.domain.study.entity.*;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StudyUpdateDto {

    private Long id;

    private String name;

    @JsonProperty("category_id")
    private Long categoryId;

    private StudyType type;

    private String location;

    @JsonProperty("recruitment_number")
    private Integer recruitmentNumber;

    private Visibility visibility;

    @JsonProperty("join_type")
    private JoinType joinType;

    private String introduce;

    public Study toEntity(Category category) {
        return Study.builder()
                .name(name)
                .category(category)
                .location(location)
                .type(type)
                .recruitmentNumber(recruitmentNumber)
                .visibility(visibility)
                .joinType(joinType)
                .introduce(introduce)
                .build();
    }
}
