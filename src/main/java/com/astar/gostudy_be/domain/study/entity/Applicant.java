package com.astar.gostudy_be.domain.study.entity;

import com.astar.gostudy_be.domain.BaseTimeEntity;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Applicant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String message;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "study_id")
    @ManyToOne
    Study study;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @ManyToOne
    Account account;

    @Builder
    public Applicant(String message, Study study, Account account) {
        this.message = message;
        this.study = study;
        this.account = account;
    }
}
