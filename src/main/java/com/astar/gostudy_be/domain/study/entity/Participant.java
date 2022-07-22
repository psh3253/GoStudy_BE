package com.astar.gostudy_be.domain.study.entity;

import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private Account account;

    @JoinColumn(name = "study_id")
    @ManyToOne
    private Study study;

    @Builder
    public Participant(Account account, Study study) {
        this.study = study;
        this.account = account;
    }
}
