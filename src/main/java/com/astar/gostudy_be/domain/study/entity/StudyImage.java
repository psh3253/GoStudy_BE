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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String filename;

    @Column
    @NotNull
    private String originalFileName;

    @Builder
    public StudyImage(Long id, String filename, String originalFileName) {
        this.id = id;
        this.filename = filename;
        this.originalFileName = originalFileName;
    }
}
