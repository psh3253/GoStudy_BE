package com.astar.gostudy_be.domain.study.repository;

import com.astar.gostudy_be.domain.study.entity.Participant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ParticipantRepositoryTest {

    @Autowired
    ParticipantRepository participantRepository;

    @Test
    @DisplayName("특정 스터디의 모든 참석자 조회")
    void findAllByStudyId() {
        // 참석자가 존재하는 경우
        {
            // given
            String studyName = "스터디명 1";
            String email = "이메일 1";
            Long studyId = 1L;

            // when
            Participant participant = participantRepository.findAllByStudyId(studyId).get(0);

            // then
            assertThat(participant.getStudy().getName()).isEqualTo(studyName);
            assertThat(participant.getAccount().getEmail()).isEqualTo(email);
        }
        // 참석자가 존재하지 않는 경우
        {
            // given
            Long studyId = 2L;

            // when
            Integer count = participantRepository.findAllByStudyId(studyId).size();

            // then
            assertThat(count).isEqualTo(0);
        }
        // 스터디가 존재하지 않는 경우
        {
            // given
            Long studyId = -1L;

            // when
            Integer count = participantRepository.findAllByStudyId(studyId).size();

            // then
            assertThat(count).isEqualTo(0);
        }
    }

    @Test
    @DisplayName("특정 스터디에 소속된 특정 이메일을 가진 참석자 조회")
    void findByStudyIdAndAccountEmail() {
        // 참석자가 존재하는 경우
        {
            // given
            Long studyId = 1L;
            String studyName = "스터디명 1";
            String email = "이메일 1";

            // when
            Participant participant = participantRepository.findByStudyIdAndAccountEmail(studyId, email).orElse(null);

            // then
            assertThat(participant.getStudy().getName()).isEqualTo(studyName);
            assertThat(participant.getAccount().getEmail()).isEqualTo(email);
        }
        // 참석자가 존재하지 않는 경우
        {
            // given
            Long studyId = 1L;
            String email = "이메일 2";

            // when
            Participant participant = participantRepository.findByStudyIdAndAccountEmail(studyId, email).orElse(null);

            // then
            assertThat(participant).isEqualTo(null);
        }
        // 스터디가 존재하지 않는 경우
        {
            // given
            Long studyId = -1L;
            String email = "이메일 1";

            // when
            Participant participant = participantRepository.findByStudyIdAndAccountEmail(studyId, email).orElse(null);

            // then
            assertThat(participant).isEqualTo(null);
        }
        // 사용자가 존재하지 않는 경우
        {
            // given
            Long studyId = 1L;
            String email = "잘못된 이메일";

            // when
            Participant participant = participantRepository.findByStudyIdAndAccountEmail(studyId, email).orElse(null);

            // then
            assertThat(participant).isEqualTo(null);
        }
    }
}