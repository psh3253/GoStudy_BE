package com.astar.gostudy_be.domain.study.entity;

import com.astar.gostudy_be.domain.user.entity.Account;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
public class ParticipantEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "evaluating_user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @NotNull
    private Account evaluatingUser;

    @JoinColumn(name = "evaluated_user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @NotNull
    private Account evaluatedUser;

    @Column
    @NotNull
    private Integer score;

    @Builder
    public ParticipantEvaluation(Account evaluatingUser, Account evaluatedUser, Integer score) {
        this.evaluatingUser = evaluatingUser;
        this.evaluatedUser = evaluatedUser;
        this.score = score;
    }
}
