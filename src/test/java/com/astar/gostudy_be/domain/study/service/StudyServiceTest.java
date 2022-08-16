package com.astar.gostudy_be.domain.study.service;

import com.astar.gostudy_be.domain.study.dto.CategoryListDto;
import com.astar.gostudy_be.domain.study.dto.StudyCreateDto;
import com.astar.gostudy_be.domain.study.dto.StudyDto;
import com.astar.gostudy_be.domain.study.dto.StudyListDto;
import com.astar.gostudy_be.domain.study.entity.*;
import com.astar.gostudy_be.domain.study.repository.CategoryRepository;
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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudyServiceTest {

    @InjectMocks
    StudyService studyService;

    @Mock
    StudyRepository studyRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    ParticipantRepository participantRepository;

    @Test
    @DisplayName("스터디 목록 조회")
    void findAllStudies() {
        // given
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
        studies.add(Study.builder()
                .name(name)
                .image(filename)
                .category(Category.builder()
                        .name(categoryName)
                        .build())
                .location(location)
                .type(type)
                .currentNumber(currentNumber)
                .recruitmentNumber(recruitmentNumber)
                .joinType(joinType)
                .introduce(introduce)
                .accessUrl("URL 1")
                .isRecruiting(true)
                .visibility(Visibility.PUBLIC)
                .account(Account.builder()
                        .email("이메일 1")
                        .password("비밀번호 1")
                        .nickname("닉네임 1")
                        .image("이미지 1")
                        .introduce("소개 1")
                        .refreshToken("리프레쉬 토큰 1")
                        .build())
                .build());

        when(studyRepository.findAllByIsRecruitingIsTrueAndVisibilityEquals(Visibility.PUBLIC)).thenReturn(studies);

        // when
        List<StudyListDto> studyListDtos = studyService.findAllStudies();

        // then
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
        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder()
                .name("카테고리 1")
                .build());

        categories.add(Category.builder()
                .name("카테고리 2")
                .build());

        when(categoryRepository.findAll()).thenReturn(categories);
        String name1 = "카테고리 1";
        String name2 = "카테고리 2";

        // when
        List<CategoryListDto> categoryListDtos = studyService.findAllCategories();

        // then
        assertThat(categoryListDtos.get(0).getName()).isEqualTo(name1);
        assertThat(categoryListDtos.get(1).getName()).isEqualTo(name2);
    }

    @Test
    @DisplayName("특정 사용자가 소속된 스터디 목록 조회")
    void findAllStudiesByAccount() {
        // given
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
        studies.add(Study.builder()
                .name(name)
                .image(filename)
                .category(Category.builder()
                        .name(categoryName)
                        .build())
                .location(location)
                .type(type)
                .currentNumber(currentNumber)
                .recruitmentNumber(recruitmentNumber)
                .joinType(joinType)
                .introduce(introduce)
                .accessUrl("URL 1")
                .isRecruiting(true)
                .visibility(Visibility.PUBLIC)
                .account(Account.builder()
                        .email("이메일 1")
                        .password("비밀번호 1")
                        .nickname("닉네임 1")
                        .image("이미지 1")
                        .introduce("소개 1")
                        .refreshToken("리프레쉬 토큰 1")
                        .build())
                .build());
        Account account = Account.builder()
                .email("이메일 2")
                .password("비밀번호 2")
                .nickname("닉네임 2")
                .image("이미지 2")
                .introduce("소개 2")
                .refreshToken("리프레쉬 토큰 2")
                .build();
        when(studyRepository.findAllByAccountEmail("이메일 2")).thenReturn(studies);

        // when
        List<StudyListDto> studyListDtos = studyService.findAllStudiesByAccount(account);

        // then
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
        Long studyId = 1L;
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

        Study study = Study.builder()
                .name(name)
                .image(filename)
                .category(Category.builder()
                        .id(categoryId)
                        .name(categoryName)
                        .build())
                .location(location)
                .type(type)
                .currentNumber(currentNumber)
                .recruitmentNumber(recruitmentNumber)
                .joinType(joinType)
                .introduce(introduce)
                .accessUrl(accessUrl)
                .isRecruiting(isRecruiting)
                .visibility(visibility)
                .account(Account.builder()
                        .email(email)
                        .password("비밀번호 1")
                        .nickname("닉네임 1")
                        .image("이미지 1")
                        .introduce("소개 1")
                        .refreshToken("리프레쉬 토큰 1")
                        .build())
                .build();

        when(studyRepository.findById(studyId)).thenReturn(Optional.ofNullable(study));

        // when
        StudyDto studyDto = studyService.findStudyById(studyId);

        // then
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
        Study study = Study.builder()
                .id(1L)
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
                .accessUrl(accessUrl)
                .isRecruiting(true)
                .visibility(Visibility.PUBLIC)
                .account(Account.builder()
                        .email("이메일 1")
                        .password("비밀번호 1")
                        .nickname("닉네임 1")
                        .image("이미지 1")
                        .introduce("소개 1")
                        .refreshToken("리프레쉬 토큰 1")
                        .build())
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
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        Category category = Category.builder()
                .name("카테고리 1")
                .build();
        Study study = studyCreateDto.toEntity(category, account, "파일명 1", "URL 1").update(Study.builder().id(1L).build());
        when(studyRepository.save(any(Study.class))).thenReturn(study);
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));

        // when
        Long studyId = studyService.createStudy(studyCreateDto, account);

        // then
        assertThat(studyId).isEqualTo(id);
    }

    @Test
    @DisplayName("스터디 수정")
    void updateStudy() {

    }

    @Test
    @DisplayName("스터디 삭제")
    void deleteStudy() {
    }

    @Test
    @DisplayName("스터디 마감")
    void closeStudy() {
    }

    @Test
    @DisplayName("스터치 참가")
    void participateStudy() {
    }

    @Test
    @DisplayName("스터디 탈퇴")
    void withdrawStudy() {
    }

    @Test
    @DisplayName("스터치 참가 신청 취소")
    void cancelApplicationStudy() {
    }
}