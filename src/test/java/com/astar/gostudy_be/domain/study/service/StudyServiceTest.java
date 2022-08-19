package com.astar.gostudy_be.domain.study.service;

import com.astar.gostudy_be.domain.study.dto.*;
import com.astar.gostudy_be.domain.study.entity.*;
import com.astar.gostudy_be.domain.study.repository.ApplicantRepository;
import com.astar.gostudy_be.domain.study.repository.CategoryRepository;
import com.astar.gostudy_be.domain.study.repository.ParticipantRepository;
import com.astar.gostudy_be.domain.study.repository.StudyRepository;
import com.astar.gostudy_be.domain.user.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @InjectMocks
    StudyService studyService;

    @Mock
    StudyRepository studyRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    ParticipantRepository participantRepository;

    @Mock
    ApplicantRepository applicantRepository;

    @Test
    @DisplayName("스터디 목록 조회")
    void findAllStudies() {
        // given
        Long id = 1L;
        String name = "스터디명 1";
        String filename = "파일명 1";
        String categoryName = "카테고리 1";
        String location = "장소 1";
        StudyType type = StudyType.OFFLINE;
        Integer currentNumber = 0;
        Integer recruitmentNumber = 10;
        JoinType joinType = JoinType.FREE;
        String introduce = "소개 1";

        List<Study> studies = new ArrayList<>();
        Category category = Category.builder()
                .id(1L)
                .name(categoryName)
                .build();
        Account account = Account.builder()
                .id(1L)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        Study study = Study.builder()
                .id(id)
                .name(name)
                .image(filename)
                .category(category)
                .location(location)
                .type(type)
                .currentNumber(currentNumber)
                .recruitmentNumber(recruitmentNumber)
                .joinType(joinType)
                .introduce(introduce)
                .accessUrl("URL 1")
                .isRecruiting(true)
                .visibility(Visibility.PUBLIC)
                .account(account)
                .build();
        studies.add(study);
        when(studyRepository.findAllByIsRecruitingIsTrueAndVisibilityEquals(Visibility.PUBLIC)).thenReturn(studies);

        // when
        List<StudyListDto> studyListDtos = studyService.findAllStudies();

        // then
        assertThat(studyListDtos.get(0).getId()).isEqualTo(id);
        assertThat(studyListDtos.get(0).getName()).isEqualTo(name);
        assertThat(studyListDtos.get(0).getFilename()).isEqualTo(filename);
        assertThat(studyListDtos.get(0).getCategoryName()).isEqualTo(categoryName);
        assertThat(studyListDtos.get(0).getType()).isEqualTo(type);
        assertThat(studyListDtos.get(0).getLocation()).isEqualTo(location);
        assertThat(studyListDtos.get(0).getCurrentNumber()).isEqualTo(currentNumber);
        assertThat(studyListDtos.get(0).getRecruitmentNumber()).isEqualTo(recruitmentNumber);
        assertThat(studyListDtos.get(0).getJoinType()).isEqualTo(joinType);
        assertThat(studyListDtos.get(0).getIntroduce()).isEqualTo(introduce);
    }

    @Test
    @DisplayName("모든 카테고리 조회")
    void findAllCategories() {
        // given
        Long id = 1L;
        String name = "카테고리 1";

        Category category = Category.builder()
                .id(id)
                .name(name)
                .build();
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(categoryRepository.findAll()).thenReturn(categories);

        // when
        List<CategoryListDto> categoryListDtos = studyService.findAllCategories();

        // then
        assertThat(categoryListDtos.get(0).getId()).isEqualTo(id);
        assertThat(categoryListDtos.get(0).getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("특정 사용자가 소속된 스터디 목록 조회")
    void findAllStudiesByAccount() {
        // given
        Long id = 1L;
        String name = "스터디명 1";
        String filename = "파일명 1";
        String categoryName = "카테고리 1";
        String location = "장소 1";
        StudyType type = StudyType.OFFLINE;
        Integer currentNumber = 0;
        Integer recruitmentNumber = 10;
        JoinType joinType = JoinType.FREE;
        String introduce = "소개 1";
        String participantEmail = "이메일 2";

        Account account = Account.builder()
                .id(1L)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        Category category = Category.builder()
                .id(1L)
                .name(categoryName)
                .build();
        Study study = Study.builder()
                .id(id)
                .name(name)
                .image(filename)
                .category(category)
                .location(location)
                .type(type)
                .currentNumber(currentNumber)
                .recruitmentNumber(recruitmentNumber)
                .joinType(joinType)
                .introduce(introduce)
                .accessUrl("URL 1")
                .isRecruiting(true)
                .visibility(Visibility.PUBLIC)
                .account(account)
                .build();
        List<Study> studies = new ArrayList<>();
        studies.add(study);
        Account participantAccount = Account.builder()
                .id(2L)
                .email(participantEmail)
                .password("비밀번호 2")
                .nickname("닉네임 2")
                .image("이미지 2")
                .introduce("소개 2")
                .refreshToken("리프레쉬 토큰 2")
                .build();
        when(studyRepository.findAllByAccountEmail(participantEmail)).thenReturn(studies);

        // when
        List<StudyListDto> studyListDtos = studyService.findAllStudiesByAccount(participantAccount);

        // then
        assertThat(studyListDtos.get(0).getId()).isEqualTo(id);
        assertThat(studyListDtos.get(0).getName()).isEqualTo(name);
        assertThat(studyListDtos.get(0).getFilename()).isEqualTo(filename);
        assertThat(studyListDtos.get(0).getCategoryName()).isEqualTo(categoryName);
        assertThat(studyListDtos.get(0).getType()).isEqualTo(type);
        assertThat(studyListDtos.get(0).getLocation()).isEqualTo(location);
        assertThat(studyListDtos.get(0).getCurrentNumber()).isEqualTo(currentNumber);
        assertThat(studyListDtos.get(0).getRecruitmentNumber()).isEqualTo(recruitmentNumber);
        assertThat(studyListDtos.get(0).getJoinType()).isEqualTo(joinType);
        assertThat(studyListDtos.get(0).getIntroduce()).isEqualTo(introduce);
    }

    @Test
    @DisplayName("특정 스터디 조회")
    void findStudyById() {
        // given
        Long id = 1L;
        String name = "스터디명 1";
        String filename = "파일명 1";
        Long categoryId = 1L;
        String categoryName = "카테고리 1";
        String location = "장소 1";
        StudyType type = StudyType.OFFLINE;
        Integer currentNumber = 0;
        Integer recruitmentNumber = 10;
        JoinType joinType = JoinType.FREE;
        String introduce = "소개 1";
        String accessUrl = "URL 1";
        Visibility visibility = Visibility.PUBLIC;
        String email = "이메일 1";
        Boolean isRecruiting = true;

        Category category = Category.builder()
                .id(categoryId)
                .name(categoryName)
                .build();
        Account account = Account.builder()
                .id(1L)
                .email(email)
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        Study study = Study.builder()
                .id(id)
                .name(name)
                .image(filename)
                .category(category)
                .location(location)
                .type(type)
                .currentNumber(currentNumber)
                .recruitmentNumber(recruitmentNumber)
                .joinType(joinType)
                .introduce(introduce)
                .accessUrl(accessUrl)
                .isRecruiting(isRecruiting)
                .visibility(visibility)
                .account(account)
                .build();

        when(studyRepository.findById(id)).thenReturn(Optional.ofNullable(study));

        // when
        StudyDto studyDto = studyService.findStudyById(id);

        // then
        assertThat(studyDto.getId()).isEqualTo(id);
        assertThat(studyDto.getName()).isEqualTo(name);
        assertThat(studyDto.getFilename()).isEqualTo(filename);
        assertThat(studyDto.getCategoryName()).isEqualTo(categoryName);
        assertThat(studyDto.getJoinType()).isEqualTo(joinType);
        assertThat(studyDto.getIntroduce()).isEqualTo(introduce);
        assertThat(studyDto.getCurrentNumber()).isEqualTo(currentNumber);
        assertThat(studyDto.getRecruitmentNumber()).isEqualTo(recruitmentNumber);
        assertThat(studyDto.getLocation()).isEqualTo(location);
        assertThat(studyDto.getAccessUrl()).isEqualTo(accessUrl);
        assertThat(studyDto.getVisibility()).isEqualTo(visibility);
        assertThat(studyDto.getCreatorEmail()).isEqualTo(email);
        assertThat(studyDto.getCategoryId()).isEqualTo(categoryId);
        assertThat(studyDto.getIsRecruiting()).isEqualTo(isRecruiting);
    }

    @Test
    @DisplayName("특정 접근 URL을 가진 스터디 ID 조회")
    void findStudyIdByAccessUrl() {
        // given
        Long id = 1L;
        String accessUrl = "URL 1";
        Category category = Category.builder()
                .id(1L)
                .name("카테고리 1")
                .build();
        Account account = Account.builder()
                .id(1L)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        Study study = Study.builder()
                .id(id)
                .name("스터디명 1")
                .image("파일명 1")
                .category(category)
                .location("장소 1")
                .type(StudyType.OFFLINE)
                .currentNumber(0)
                .recruitmentNumber(10)
                .joinType(JoinType.FREE)
                .introduce("소개 1")
                .accessUrl(accessUrl)
                .isRecruiting(true)
                .visibility(Visibility.PUBLIC)
                .account(account)
                .build();

        when(studyRepository.findStudyByAccessUrl(accessUrl)).thenReturn(Optional.ofNullable(study));

        // when
        Long studyId = studyService.findStudyIdByAccessUrl(accessUrl);

        // then
        assertThat(studyId).isEqualTo(id);
    }

    @Test
    @DisplayName("스터디 생성")
    void createStudy() {
        // given
        Long id = 1L;
        StudyCreateDto studyCreateDto = new StudyCreateDto("스터디명 1", null, 1L, StudyType.OFFLINE, "장소 1", 10, Visibility.PUBLIC, JoinType.FREE, "소개 1");
        Account account = Account.builder()
                .id(1L)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        Category category = Category.builder()
                .id(1L)
                .name("카테고리 1")
                .build();
        Study study = studyCreateDto.toEntity(category, account, "파일명 1", "URL 1").update(Study.builder().id(id).build());

        when(studyRepository.save(any(Study.class))).thenReturn(study);
        when(categoryRepository.findById(id)).thenReturn(Optional.ofNullable(category));

        // when
        Long studyId = studyService.createStudy(studyCreateDto, account);

        // then
        assertThat(studyId).isEqualTo(id);
    }

    @Test
    @DisplayName("스터디 수정")
    void updateStudy() {
        // given
        Long id = 1L;
        StudyUpdateDto studyUpdateDto = new StudyUpdateDto(id, "스터디명 2", 1L, StudyType.OFFLINE, "장소 2", 5, Visibility.PRIVATE, JoinType.APPROVAL, "소개 2");
        Account account = Account.builder()
                .id(1L)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        Category category = Category.builder()
                .id(1L)
                .name("카테고리 1")
                .build();
        Study study = Study.builder()
                .id(id)
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
        when(studyRepository.findById(id)).thenReturn(Optional.ofNullable(study));
        when(studyRepository.save(any(Study.class))).thenReturn(study.update(studyUpdateDto.toEntity(category)));
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        // when
        Long studyId = studyService.updateStudy(studyUpdateDto, id, account);

        // then
        assertThat(studyId).isEqualTo(studyId);
    }

    @Test
    @DisplayName("스터디 삭제")
    void deleteStudy() {
        // given
        Long id = 1L;
        Account account = Account.builder()
                .id(1L)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        Category category = Category.builder()
                .id(1L)
                .name("카테고리 1")
                .build();
        Study study = Study.builder()
                .id(id)
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
        when(studyRepository.findById(id)).thenReturn(Optional.ofNullable(study));

        // when
        Long studyId = studyService.deleteStudy(id, account);

        // then
        assertThat(studyId).isEqualTo(id);
    }

    @Test
    @DisplayName("스터디 마감")
    void closeStudy() {
        // given
        Long id = 1L;
        Account account = Account.builder()
                .id(1L)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        Category category = Category.builder()
                .id(1L)
                .name("카테고리 1")
                .build();
        Study study = Study.builder()
                .id(id)
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
        when(studyRepository.findById(id)).thenReturn(Optional.ofNullable(study));
        when(studyRepository.save(any(Study.class))).thenReturn(study);

        // when
        Long studyId = studyService.closeStudy(id, account);

        // then
        assertThat(studyId).isEqualTo(id);
    }

    @Test
    @DisplayName("스터치 참가")
    void participateStudy() {
        // 스터디의 가입 유형이 자유인 경우
        {
            // given
            Long id = 1L;
            Long studyId = 1L;
            Account account = Account.builder()
                    .id(1L)
                    .email("이메일 1")
                    .password("비밀번호 1")
                    .nickname("닉네임 1")
                    .image("이미지 1")
                    .introduce("소개 1")
                    .refreshToken("리프레쉬 토큰 1")
                    .build();
            Account participantAccount = Account.builder()
                    .id(1L)
                    .email("이메일 2")
                    .password("비밀번호 2")
                    .nickname("닉네임 2")
                    .image("이미지 2")
                    .introduce("소개 2")
                    .refreshToken("리프레쉬 토큰 2")
                    .build();
            Category category = Category.builder()
                    .id(1L)
                    .name("카테고리 1")
                    .build();
            Study study = Study.builder()
                    .id(studyId)
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
            Participant participant = Participant.builder()
                    .id(id)
                    .account(participantAccount)
                    .study(study)
                    .build();

            when(studyRepository.findById(studyId)).thenReturn(Optional.ofNullable(study));
            when(participantRepository.save(any(Participant.class))).thenReturn(participant);

            // when
            Long participantId = studyService.participateStudy(studyId, null, participantAccount);

            // then
            assertThat(participantId).isEqualTo(id);
        }
        // 스터디의 가입 유형이 승인인 경우
        {
            Long id = 1L;
            Long studyId = 1L;
            String message = "메시지 1";
            Account account = Account.builder()
                    .id(1L)
                    .email("이메일 1")
                    .password("비밀번호 1")
                    .nickname("닉네임 1")
                    .image("이미지 1")
                    .introduce("소개 1")
                    .refreshToken("리프레쉬 토큰 1")
                    .build();
            Account applicantAccount = Account.builder()
                    .id(1L)
                    .email("이메일 2")
                    .password("비밀번호 2")
                    .nickname("닉네임 2")
                    .image("이미지 2")
                    .introduce("소개 2")
                    .refreshToken("리프레쉬 토큰 2")
                    .build();
            Study study = Study.builder()
                    .id(studyId)
                    .name("스터디명 1")
                    .image("파일명 1")
                    .category(Category.builder()
                            .name("카테고리 1")
                            .build())
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
            Applicant applicant = Applicant.builder()
                    .id(id)
                    .account(applicantAccount)
                    .study(study)
                    .message(message)
                    .build();

            when(studyRepository.findById(studyId)).thenReturn(Optional.ofNullable(study));
            when(applicantRepository.save(any(Applicant.class))).thenReturn(applicant);

            // when
            Long applicantId = studyService.participateStudy(studyId, "메시지 1", applicantAccount);

            // then
            assertThat(applicantId).isEqualTo(id);
        }
    }

    @Test
    @DisplayName("스터디 탈퇴")
    void withdrawStudy() {
        // given
        Long id = 1L;
        Long studyId = 1L;
        Account account = Account.builder()
                .id(1L)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        Account participantAccount = Account.builder()
                .id(id)
                .email("이메일 2")
                .password("비밀번호 2")
                .nickname("닉네임 2")
                .image("이미지 2")
                .introduce("소개 2")
                .refreshToken("리프레쉬 토큰 2")
                .build();
        Study study = Study.builder()
                .id(studyId)
                .name("스터디명 1")
                .image("파일명 1")
                .category(Category.builder()
                        .name("카테고리 1")
                        .build())
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
        Participant participant = Participant.builder()
                .id(id)
                .account(participantAccount)
                .study(study)
                .build();
        when(participantRepository.findByStudyIdAndAccountEmail(studyId, "이메일 1")).thenReturn(Optional.ofNullable(participant));
        when(studyRepository.findById(studyId)).thenReturn(Optional.ofNullable(study));

        // when
        Long participantId = studyService.withdrawStudy(studyId, account);

        // then
        assertThat(participantId).isEqualTo(id);
    }

    @Test
    @DisplayName("스터치 참가 신청 취소")
    void cancelApplicationStudy() {
        // given
        Long id = 1L;
        Long studyId = 1L;
        String email = "이메일 2";
        Account applicantAccount = Account.builder()
                .id(1L)
                .email(email)
                .password("비밀번호 2")
                .nickname("닉네임 2")
                .image("이미지 2")
                .introduce("소개 2")
                .refreshToken("리프레쉬 토큰 2")
                .build();
        Account account = Account.builder()
                .id(1L)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        Study study = Study.builder()
                .id(id)
                .name("스터디명 1")
                .image("파일명 1")
                .category(Category.builder()
                        .name("카테고리 1")
                        .build())
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
        Applicant applicant = Applicant.builder()
                .id(id)
                .account(applicantAccount)
                .study(study)
                .message("메시지 1")
                .build();
        when(applicantRepository.findByStudyIdAndAccountEmail(studyId, email)).thenReturn(Optional.ofNullable(applicant));

        // when
        Long applicantId = studyService.cancelApplicationStudy(studyId, applicantAccount);

        // then
        assertThat(applicantId).isEqualTo(id);
    }
}