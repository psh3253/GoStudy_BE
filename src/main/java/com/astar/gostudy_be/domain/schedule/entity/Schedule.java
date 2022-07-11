package com.astar.gostudy_be.domain.schedule.entity;

import com.astar.gostudy_be.domain.BaseTimeEntity;
import com.astar.gostudy_be.domain.user.entity.User;
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
    private User user;

    @Builder
    public Schedule(LocalDateTime dateTime, String content, String location, User user) {
        this.dateTime = dateTime;
        this.content = content;
        this.location = location;
        this.user = user;
    }
}
