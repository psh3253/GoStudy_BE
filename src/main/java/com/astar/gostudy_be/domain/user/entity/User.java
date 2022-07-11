package com.astar.gostudy_be.domain.user.entity;

import com.astar.gostudy_be.domain.BaseTimeEntity;
import com.astar.gostudy_be.domain.study.entity.Category;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String nickname;

    @Column
    @NotNull
    private String email;

    @Column
    private String image;

    @Column
    private String introduce;

    @JoinColumn(name = "interest_category_id")
    @ManyToOne
    private Category category;

    @Builder
    public User(String nickname, String email, String image, String introduce, Category category) {
        this.nickname = nickname;
        this.email = email;
        this.image = image;
        this.introduce = introduce;
        this.category = category;
    }
}
