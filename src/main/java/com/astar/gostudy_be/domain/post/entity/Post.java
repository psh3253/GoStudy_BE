package com.astar.gostudy_be.domain.post.entity;

import com.astar.gostudy_be.domain.BaseTimeEntity;
import com.astar.gostudy_be.domain.study.entity.Study;
import com.astar.gostudy_be.domain.user.entity.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "study_id")
    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    private Study study;

    @Column
    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String content;

    @Column
    private String image;

    @JoinColumn(name = "creator_id")
    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    private User user;

    @Builder
    public Post(Study study, String title, String content, String image, User user) {
        this.study = study;
        this.title = title;
        this.content = content;
        this.image = image;
        this.user = user;
    }
}
