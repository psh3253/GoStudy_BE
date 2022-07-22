package com.astar.gostudy_be.domain.study.entity;

import com.astar.gostudy_be.domain.BaseTimeEntity;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Study extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String name;

    @JoinColumn(name = "category_id")
    @ManyToOne(cascade = CascadeType.ALL)
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
    private int recruitment_number;

    @Column
    @NotNull
    private Visibility visibility;

    @Column
    @NotNull
    private JoinType joinType;

    @Column
    private String introduce;

    @JoinColumn(name = "creator_id")
    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    private Account account;

    @Builder
    public Study(String name, Category category, StudyType type, String location, int recruitment_number, Visibility visibility, JoinType joinType, String introduce, Account account) {
        this.name = name;
        this.category = category;
        this.type = type;
        this.location = location;
        this.recruitment_number = recruitment_number;
        this.visibility = visibility;
        this.joinType = joinType;
        this.introduce = introduce;
        this.account = account;
    }
}
