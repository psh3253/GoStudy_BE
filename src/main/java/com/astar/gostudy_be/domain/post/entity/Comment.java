package com.astar.gostudy_be.domain.post.entity;

import com.astar.gostudy_be.domain.BaseTimeEntity;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @JoinColumn(name = "post_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Post post;

    @JoinColumn(name = "creator_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Account account;

    @Builder
    public Comment(Long id, String content, Post post, Account account) {
        this.id = id;
        this.content = content;
        this.post = post;
        this.account = account;
    }
}
