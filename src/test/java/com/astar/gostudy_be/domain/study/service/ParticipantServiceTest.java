package com.astar.gostudy_be.domain.study.service;

import com.astar.gostudy_be.domain.study.dto.ParticipantListDto;
import com.astar.gostudy_be.domain.study.entity.*;
import com.astar.gostudy_be.domain.study.repository.ParticipantRepository;
import com.astar.gostudy_be.domain.study.repository.StudyRepository;
import com.astar.gostudy_be.domain.user.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @InjectMocks
    ParticipantService participantService;

    @Mock
    ParticipantRepository participantRepository;

    @Mock
    StudyRepository studyRepository;

    Account account = null;
    Category category = null;
    Study study = null;

    @BeforeEach
    void setup() {
        account = Account.builder()
                .id(1L)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        category = Category.builder()
                .id(1L)
                .name("카테고리 1")
                .build();
        study = Study.builder()
                .name("스터디명 1")
                .image("파일명 1")
                .category(category)
                .location("장소 1")
                .type(StudyType.OFFLINE)
                .currentNumber(0)
                .recruitmentNumber(10)
                .joinType(JoinType.FREE)
                .introduce("소개 1")
                .accessUrl("URL 1")
                .isRecruiting(true)
                .visibility(Visibility.PUBLIC)
                .account(account)
                .build();
    }

    @Test
    @DisplayName("특정 스터디의 모든 참석자 조회")
    void findAllParticipantsByStudyId() {
        // given
        Long studyId = 1L;
        Long participantId = 1L;
        String email = "이메일 2";
        String nickname = "닉네임 2";
        String image = "이미지 2";
        String introduce = "소개 2";

        ReflectionTestUtils.setField(study, "id", studyId);
        Account participantAccount = Account.builder()
                .id(2L)
                .email(email)
                .password("비밀번호 2")
                .nickname(nickname)
                .image(image)
                .introduce(introduce)
                .refreshToken("리프레쉬 토큰 2")
                .build();
        Participant participant = Participant.builder()
                .study(study)
                .account(participantAccount)
                .build();
        ReflectionTestUtils.setField(participant, "id", participantId);
        List<Participant> participants = new ArrayList<>();
        participants.add(participant);

        when(participantRepository.findAllByStudyId(studyId)).thenReturn(participants);

        // when
        List<ParticipantListDto> participantListDtos = participantService.findAllParticipantsByStudyId(studyId);

        // then
        assertThat(participantListDtos.get(0).getEmail()).isEqualTo(email);
        assertThat(participantListDtos.get(0).getNickname()).isEqualTo(nickname);
        assertThat(participantListDtos.get(0).getImage()).isEqualTo(image);
        assertThat(participantListDtos.get(0).getIntroduce()).isEqualTo(introduce);
    }

    @Test
    @DisplayName("스터디 강제 퇴장")
    void deleteParticipant() {
        // given
        Long id = 1L;
        Long studyId = 1L;

        ReflectionTestUtils.setField(study, "id", studyId);
        Account participantAccount = Account.builder()
                .id(2L)
                .email("이메일 2")
                .password("비밀번호 2")
                .nickname("닉네임 2")
                .image("이미지 2")
                .introduce("소개 2")
                .refreshToken("리프레쉬 토큰 2")
                .build();
        Participant participant = Participant.builder()
                .study(study)
                .account(participantAccount)
                .build();
        ReflectionTestUtils.setField(participant, "id", id);
        when(participantRepository.findById(id)).thenReturn(Optional.ofNullable(participant));

        // when
        Long participantId = participantService.deleteParticipant(id, account);

        // then
        assertThat(participantId).isEqualTo(id);
    }
}