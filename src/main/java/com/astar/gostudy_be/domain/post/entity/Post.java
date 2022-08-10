package com.astar.gostudy_be.domain.post.entity;

import com.astar.gostudy_be.domain.BaseTimeEntity;
import com.astar.gostudy_be.domain.study.entity.Study;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "study_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    @Column
    @NotNull
    @ColumnDefault("0")
    private Long commentCount;

    @JoinColumn(name = "creator_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Account account;

    @Builder
    public Post(Study study, String title, String content, String image, Long commentCount, Account account) {
        this.study = study;
        this.title = title;
        this.content = content;
        this.image = image;
        this.commentCount = commentCount;
        this.account = account;
    }
}
