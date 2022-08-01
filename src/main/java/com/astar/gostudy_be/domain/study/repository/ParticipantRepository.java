package com.astar.gostudy_be.domain.study.repository;

import com.astar.gostudy_be.domain.study.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findAllByStudyId(Long studyId);
}
