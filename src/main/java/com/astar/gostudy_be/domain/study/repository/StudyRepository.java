package com.astar.gostudy_be.domain.study.repository;

import com.astar.gostudy_be.domain.study.entity.Study;
import com.astar.gostudy_be.domain.study.entity.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {
    List<Study> findAllByIsRecruitingIsTrueAndVisibilityEquals(Visibility visibility);

    Long countByAccessUrl(String accessUrl);

    Optional<Study> findStudyByAccessUrl(String accessUrl);

    @Query(value = "SELECT s.* FROM study s WHERE s.id IN (SELECT p.study_id FROM participant p WHERE p.user_id = (SELECT a.id FROM account a WHERE a.email = :email))", nativeQuery = true)
    List<Study> findAllByAccountEmail(String email);
}
