package com.astar.gostudy_be.domain.study.entity;

import com.astar.gostudy_be.domain.BaseTimeEntity;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class StudyImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String filename;

    @Builder
    public StudyImage(Long id, String filename) {
        this.id = id;
        this.filename = filename;
    }
}
