package com.astar.gostudy_be.domain.study.repository;

import com.astar.gostudy_be.domain.study.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    List<Applicant> findAllByStudyId(Long studyId);
}
