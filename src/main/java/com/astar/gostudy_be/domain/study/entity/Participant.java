package com.astar.gostudy_be.domain.study.entity;

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
public class Participant extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Account account;

    @JoinColumn(name = "study_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Study study;

    @Builder
    public Participant(Long id, Account account, Study study) {
        this.id = id;
        this.study = study;
        this.account = account;
    }
}
