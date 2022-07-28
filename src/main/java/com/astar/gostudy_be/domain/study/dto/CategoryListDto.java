package com.astar.gostudy_be.domain.study.dto;

import com.astar.gostudy_be.domain.study.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CategoryListDto {
    private Long id;

    private String name;

    public CategoryListDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
