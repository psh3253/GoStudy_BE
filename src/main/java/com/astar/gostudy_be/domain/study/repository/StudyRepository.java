package com.astar.gostudy_be.domain.study.repository;

import com.astar.gostudy_be.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {
//    @Query("SELECT s FROM Study s WHERE s.isRecruiting = true")
    List<Study> findAllByIsRecruitingIsTrue();

    List<Study> findAllByCategoryId(Long categoryId);
}
