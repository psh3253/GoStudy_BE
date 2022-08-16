package com.astar.gostudy_be.domain.study.repository;

import com.astar.gostudy_be.domain.study.entity.Applicant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ApplicantRepositoryTest {

    @Autowired
    ApplicantRepository applicantRepository;

    @Test
    @DisplayName("특정 스터디의 모든 신청자 조회")
    void findAllByStudyId() {
        // 신청자가 존재하는 경우
        {
            // given
            Long studyId = 2L;
            String email = "이메일 2";
            String studyName = "스터디명 2";
            String message = "메시지 2";

            // when
            Applicant applicant = applicantRepository.findAllByStudyId(studyId).get(0);

            // then
            assertThat(applicant.getAccount().getEmail()).isEqualTo(email);
            assertThat(applicant.getStudy().getName()).isEqualTo(studyName);
            assertThat(applicant.getMessage()).isEqualTo(message);
        }
        // 신청자가 존재하지 않는 경우
        {
            // given
            Long studyId = 1L;

            // when
            Integer count = applicantRepository.findAllByStudyId(studyId).size();

            // then
            assertThat(count).isEqualTo(0);
        }
        // 스터디가 존재하지 않는 경우
        {
            // given
            Long studyId = -1L;

            // when
            Integer count = applicantRepository.findAllByStudyId(studyId).size();

            // then
            assertThat(count).isEqualTo(0);
        }
    }

    @Test
    @DisplayName("특정 스터디의 특정 이메일을 가진 신청자 조회")
    void findByStudyIdAndAccountEmail() {
        // 신청자가 존재하는 경우
        {
            // given
            Long studyId = 2L;
            String email = "이메일 2";
            String studyName = "스터디명 2";
            String message = "메시지 2";

            // when
            Applicant applicant = applicantRepository.findByStudyIdAndAccountEmail(studyId, email).orElse(null);

            // then
            assertThat(applicant.getStudy().getName()).isEqualTo(studyName);
            assertThat(applicant.getAccount().getEmail()).isEqualTo(email);
            assertThat(applicant.getMessage()).isEqualTo(message);
        }
        // 신청자가 존재하지 않는 경우
        {
            // given
            Long studyId = 1L;
            String email = "이메일 1";

            // when
            Applicant applicant = applicantRepository.findByStudyIdAndAccountEmail(studyId, email).orElse(null);

            // then
            assertThat(applicant).isEqualTo(null);
        }
        // 스터디가 존재하지 않는 경우
        {
            // given
            Long studyId = -1L;
            String email = "이메일 2";

            // when
            Applicant applicant = applicantRepository.findByStudyIdAndAccountEmail(studyId, email).orElse(null);

            // then
            assertThat(applicant).isEqualTo(null);

        }
        // 사용자가 존재하지 않는 경우
        {
            // given
            Long studyId = 2L;
            String email = "잘못된 이메일";

            // when
            Applicant applicant = applicantRepository.findByStudyIdAndAccountEmail(studyId, email).orElse(null);

            // then
            assertThat(applicant).isEqualTo(null);
        }
    }
}