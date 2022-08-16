package com.astar.gostudy_be.domain.study.repository;

import com.astar.gostudy_be.domain.study.entity.JoinType;
import com.astar.gostudy_be.domain.study.entity.Study;
import com.astar.gostudy_be.domain.study.entity.StudyType;
import com.astar.gostudy_be.domain.study.entity.Visibility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudyRepositoryTest {

    @Autowired
    StudyRepository studyRepository;


    @Test
    @DisplayName("현재 모집중이고 공개중인 모든 스터디 조회")
    void findAllByIsRecruitingIsTrueAndVisibilityEquals() {
        // given
        String name = "스터디명 1";
        String filename = "파일명 1";
        String categoryName = "카테고리 1";
        String location = "장소 1";
        StudyType type = StudyType.OFFLINE;
        Integer currentNumber = 0;
        Integer recruitmentNumber = 10;
        Visibility visibility = Visibility.PUBLIC;
        JoinType joinType = JoinType.FREE;
        Boolean isRecruiting = true;
        String introduce = "소개 1";
        String accessUrl = "URL 1";
        String email = "이메일 1";

        // when
        List<Study> studies = studyRepository.findAllByIsRecruitingIsTrueAndVisibilityEquals(visibility);

        // then
        assertThat(studies.get(0).getName()).isEqualTo(name);
        assertThat(studies.get(0).getImage()).isEqualTo(filename);
        assertThat(studies.get(0).getCategory().getName()).isEqualTo(categoryName);
        assertThat(studies.get(0).getType()).isEqualTo(type);
        assertThat(studies.get(0).getLocation()).isEqualTo(location);
        assertThat(studies.get(0).getCurrentNumber()).isEqualTo(currentNumber);
        assertThat(studies.get(0).getRecruitmentNumber()).isEqualTo(recruitmentNumber);
        assertThat(studies.get(0).getVisibility()).isEqualTo(visibility);
        assertThat(studies.get(0).getJoinType()).isEqualTo(joinType);
        assertThat(studies.get(0).getIsRecruiting()).isEqualTo(isRecruiting);
        assertThat(studies.get(0).getIntroduce()).isEqualTo(introduce);
        assertThat(studies.get(0).getAccessUrl()).isEqualTo(accessUrl);
        assertThat(studies.get(0).getAccount().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("특정 Access Url을 가진 스터디의 갯수 조회")
    void countByAccessUrl() {
        // 스터디가 존재하는 경우
        {
            // given
            String accessUrl = "URL 1";

            // when
            Long count = studyRepository.countByAccessUrl(accessUrl);

            // then
            assertThat(count).isEqualTo(1);
        }
        // 스터디가 존재하지 않는 경우
        {
            // given
            String accessUrl = "잘못된 URL";

            // when
            Long count = studyRepository.countByAccessUrl(accessUrl);

            // then
            assertThat(count).isEqualTo(0);
        }
    }

    @Test
    @DisplayName("특정 Access Url을 가진 스터디 조회")
    void findStudyByAccessUrl() {
        // 스터디가 존재하는 경우
        {
            // given
            String name = "스터디명 1";
            String filename = "파일명 1";
            String categoryName = "카테고리 1";
            String location = "장소 1";
            StudyType type = StudyType.OFFLINE;
            Integer currentNumber = 0;
            Integer recruitmentNumber = 10;
            Visibility visibility = Visibility.PUBLIC;
            JoinType joinType = JoinType.FREE;
            Boolean isRecruiting = true;
            String introduce = "소개 1";
            String accessUrl = "URL 1";
            String email = "이메일 1";

            // when
            Study study = studyRepository.findStudyByAccessUrl(accessUrl).orElse(null);

            // then
            assertThat(study.getName()).isEqualTo(name);
            assertThat(study.getImage()).isEqualTo(filename);
            assertThat(study.getCategory().getName()).isEqualTo(categoryName);
            assertThat(study.getType()).isEqualTo(type);
            assertThat(study.getLocation()).isEqualTo(location);
            assertThat(study.getCurrentNumber()).isEqualTo(currentNumber);
            assertThat(study.getRecruitmentNumber()).isEqualTo(recruitmentNumber);
            assertThat(study.getVisibility()).isEqualTo(visibility);
            assertThat(study.getJoinType()).isEqualTo(joinType);
            assertThat(study.getIsRecruiting()).isEqualTo(isRecruiting);
            assertThat(study.getIntroduce()).isEqualTo(introduce);
            assertThat(study.getAccessUrl()).isEqualTo(accessUrl);
            assertThat(study.getAccount().getEmail()).isEqualTo(email);
        }
        // 스터디가 존재하지 않는 경우
        {
            // given
            String accessUrl = "잘못된 URL";

            // when
            Study study = studyRepository.findStudyByAccessUrl(accessUrl).orElse(null);

            // then
            assertThat(study).isEqualTo(null);
        }
    }

    @Test
    @DisplayName("특정 사용자가 소속된 모든 스터디 조회")
    void findAllByAccountEmail() {
        // 스터디가 존재하는 경우
        {
            // given
            String name = "스터디명 1";
            String filename = "파일명 1";
            String categoryName = "카테고리 1";
            String location = "장소 1";
            StudyType type = StudyType.OFFLINE;
            Integer currentNumber = 0;
            Integer recruitmentNumber = 10;
            Visibility visibility = Visibility.PUBLIC;
            JoinType joinType = JoinType.FREE;
            Boolean isRecruiting = true;
            String introduce = "소개 1";
            String accessUrl = "URL 1";
            String email = "이메일 1";

            // when
            Study study = studyRepository.findAllByAccountEmail(email).get(0);

            // then
            assertThat(study.getName()).isEqualTo(name);
            assertThat(study.getImage()).isEqualTo(filename);
            assertThat(study.getCategory().getName()).isEqualTo(categoryName);
            assertThat(study.getType()).isEqualTo(type);
            assertThat(study.getLocation()).isEqualTo(location);
            assertThat(study.getCurrentNumber()).isEqualTo(currentNumber);
            assertThat(study.getRecruitmentNumber()).isEqualTo(recruitmentNumber);
            assertThat(study.getVisibility()).isEqualTo(visibility);
            assertThat(study.getJoinType()).isEqualTo(joinType);
            assertThat(study.getIsRecruiting()).isEqualTo(isRecruiting);
            assertThat(study.getIntroduce()).isEqualTo(introduce);
            assertThat(study.getAccessUrl()).isEqualTo(accessUrl);
            assertThat(study.getAccount().getEmail()).isEqualTo(email);
        }
        // 스터디가 존재하지 않는 경우
        {
            // given
            String email = "이메일 2";

            // when
            Integer count = studyRepository.findAllByAccountEmail(email).size();

            //then
            assertThat(count).isEqualTo(0);
        }
        // 사용자가 존재하지 않는 경우
        {
            // given
            String email = "잘못된 이메일";

            // when
            Integer count = studyRepository.findAllByAccountEmail(email).size();

            // then
            assertThat(count).isEqualTo(0);
        }
    }
}