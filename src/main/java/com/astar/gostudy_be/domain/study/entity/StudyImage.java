package com.astar.gostudy_be.domain.study.entity;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class StudyImage {
    @Id
    private Long id;

    @Column
    @NotNull
    private String path;

    @Column
    @NotNull
    private String originalName;

    @Builder
    public StudyImage(Long id, String path, String originalName) {
        this.id = id;
        this.path = path;
        this.originalName = originalName;
    }
}
