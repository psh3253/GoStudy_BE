package com.astar.gostudy_be.domain.study.service;

import com.astar.gostudy_be.domain.study.dto.ApplicantListDto;
import com.astar.gostudy_be.domain.study.entity.*;
import com.astar.gostudy_be.domain.study.repository.ApplicantRepository;
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
class ApplicantServiceTest {

    @InjectMocks
    ApplicantService applicantService;

    @Mock
    ApplicantRepository applicantRepository;

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
                .joinType(JoinType.APPROVAL)
                .introduce("소개 1")
                .accessUrl("URL 1")
                .isRecruiting(true)
                .visibility(Visibility.PUBLIC)
                .account(account)
                .build();
    }

    @Test
    @DisplayName("특정 스터디의 모든 참석 신청자 조회")
    void findAllApplicantsByStudyId() {
        // given
        Long studyId = 1L;
        Long id = 1L;
        String email = "이메일 2";
        String nickname = "닉네임 2";
        String image = "이미지 2";
        String message = "메시지 1";

        ReflectionTestUtils.setField(study, "id", studyId);
        Account applicantAccount = Account.builder()
                .id(2L)
                .email(email)
                .password("비밀번호 2")
                .nickname(nickname)
                .image(image)
                .introduce("소개 2")
                .refreshToken("리프레쉬 토큰 2")
                .build();
        Applicant applicant = Applicant.builder()
                .account(applicantAccount)
                .study(study)
                .message(message)
                .build();
        ReflectionTestUtils.setField(applicant, "id", id);
        List<Applicant> applicants = new ArrayList<>();
        applicants.add(applicant);

        when(applicantRepository.findAllByStudyId(studyId)).thenReturn(applicants);

        // when
        List<ApplicantListDto> applicantListDtos = applicantService.findAllApplicantsByStudyId(studyId);

        // then
        assertThat(applicantListDtos.get(0).getId()).isEqualTo(id);
        assertThat(applicantListDtos.get(0).getEmail()).isEqualTo(email);
        assertThat(applicantListDtos.get(0).getNickname()).isEqualTo(nickname);
        assertThat(applicantListDtos.get(0).getImage()).isEqualTo(image);
        assertThat(applicantListDtos.get(0).getMessage()).isEqualTo(message);
        assertThat(applicantListDtos.get(0).getStudyId()).isEqualTo(studyId);
    }

    @Test
    @DisplayName("스터디 참석 신청 수락")
    void acceptParticipation() {
        // given
        Long id = 1L;
        Long studyId = 1L;

        ReflectionTestUtils.setField(study, "id", studyId);
        Account applicantAccount = Account.builder()
                .id(2L)
                .email("이메일 2")
                .password("비밀번호 2")
                .nickname("닉네임 2")
                .image("이미지 2")
                .introduce("소개 2")
                .refreshToken("리프레쉬 토큰 2")
                .build();
        Applicant applicant = Applicant.builder()
                .account(applicantAccount)
                .study(study)
                .message("메시지 1")
                .build();
        ReflectionTestUtils.setField(applicant, "id", id);

        when(applicantRepository.findById(id)).thenReturn(Optional.ofNullable(applicant));

        // when
        Long applicantId = applicantService.acceptParticipation(id, account);

        // then
        assertThat(applicantId).isEqualTo(id);
    }

    @Test
    @DisplayName("스터디 참석 신청 거절")
    void deleteApplicant() {
        // given
        Long id = 1L;
        Long studyId = 1L;

        ReflectionTestUtils.setField(study, "id", studyId);
        Account applicantAccount = Account.builder()
                .id(2L)
                .email("이메일 2")
                .password("비밀번호 2")
                .nickname("닉네임 2")
                .image("이미지 2")
                .introduce("소개 2")
                .refreshToken("리프레쉬 토큰 2")
                .build();
        Applicant applicant = Applicant.builder()
                .account(applicantAccount)
                .study(study)
                .message("메시지 1")
                .build();
        ReflectionTestUtils.setField(applicant, "id", id);
        when(applicantRepository.findById(id)).thenReturn(Optional.ofNullable(applicant));

        // when
        Long applicantId = applicantService.deleteApplicant(id, account);

        // then
        assertThat(applicantId).isEqualTo(id);
    }
}