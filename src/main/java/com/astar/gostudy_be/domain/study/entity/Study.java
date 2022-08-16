package com.astar.gostudy_be.domain.study.entity;

import com.astar.gostudy_be.domain.BaseTimeEntity;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Study extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String name;

    @Column
    private String image;

    @JoinColumn(name = "category_id")
    @ManyToOne
    @NotNull
    private Category category;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private StudyType type;

    @Column
    private String location;

    @Column
    @NotNull
    @ColumnDefault("0")
    private Integer currentNumber;

    @Column
    @NotNull
    private Integer recruitmentNumber;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private JoinType joinType;

    @Column
    @NotNull
    @ColumnDefault("true")
    private Boolean isRecruiting;

    @Column
    private String introduce;

    @Column
    @NotNull
    private String accessUrl;

    @JoinColumn(name = "creator_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Account account;

    @Builder
    public Study(Long id, String name, String image, Category category, StudyType type, String location, Integer currentNumber, Integer recruitmentNumber, Visibility visibility, JoinType joinType, Boolean isRecruiting, String introduce, String accessUrl, Account account) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.category = category;
        this.type = type;
        this.location = location;
        this.currentNumber = currentNumber;
        this.recruitmentNumber = recruitmentNumber;
        this.visibility = visibility;
        this.joinType = joinType;
        this.isRecruiting = isRecruiting;
        this.introduce = introduce;
        this.accessUrl = accessUrl;
        this.account = account;
    }

    public Study update(Study study) {
        if(study.getId() != null)
            this.id = study.getId();
        if(study.getName() != null)
            this.name = study.getName();
        if(study.getImage() != null)
            this.image = study.getImage();
        if(study.getCategory() != null)
            this.category = study.getCategory();
        if(study.getType() != null)
            this.type = study.getType();
        if(study.getLocation() != null)
            this.location = study.getLocation();
        if(study.getCurrentNumber() != null)
            this.currentNumber = study.getCurrentNumber();
        if(study.getRecruitmentNumber() != null)
            this.recruitmentNumber = study.getRecruitmentNumber();
        if(study.getVisibility() != null)
            this.visibility = study.getVisibility();
        if(study.getJoinType() != null)
            this.joinType = study.getJoinType();
        if(study.getIntroduce() != null)
            this.introduce = study.getIntroduce();
        if(study.getIsRecruiting() != null)
            this.isRecruiting = study.getIsRecruiting();
        return this;
    }
}
