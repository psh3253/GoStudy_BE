package com.astar.gostudy_be.domain.study.entity;

import com.astar.gostudy_be.domain.user.entity.User;
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
    private User user;

    @JoinColumn(name = "study_id")
    @ManyToOne
    private Study study;

    @Builder
    public Participant(User user, Study study) {
        this.study = study;
        this.user = user;
    }
}
