package com.astar.gostudy_be.domain.study.dto;

import com.astar.gostudy_be.domain.study.entity.*;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudyCreateDto {
    private String name;

    @JsonIgnore
    private MultipartFile image;

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

    public Study toEntity(Category category, Account account, String image, String accessUrl) {
        return Study.builder()
                .name(name)
                .image(image)
                .category(category)
                .location(location)
                .type(type)
                .currentNumber(1)
                .recruitmentNumber(recruitmentNumber)
                .visibility(visibility)
                .joinType(joinType)
                .introduce(introduce)
                .accessUrl(accessUrl)
                .account(account)
                .isRecruiting(true)
                .build();
    }
}
