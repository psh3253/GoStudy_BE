package com.astar.gostudy_be.domain.schedule.entity;

import com.astar.gostudy_be.domain.BaseTimeEntity;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Schedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private LocalDateTime dateTime;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String content;

    @Column
    @NotNull
    private String location;

    @JoinColumn(name = "creator_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Account account;

    @Builder
    public Schedule(LocalDateTime dateTime, String content, String location, Account account) {
        this.dateTime = dateTime;
        this.content = content;
        this.location = location;
        this.account = account;
    }
}
