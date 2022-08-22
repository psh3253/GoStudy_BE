package com.astar.gostudy_be.domain.study.controller;

import com.astar.gostudy_be.domain.study.dto.CategoryListDto;
import com.astar.gostudy_be.domain.study.dto.StudyDto;
import com.astar.gostudy_be.domain.study.dto.StudyListDto;
import com.astar.gostudy_be.domain.study.dto.StudyUpdateDto;
import com.astar.gostudy_be.domain.study.entity.*;
import com.astar.gostudy_be.domain.study.service.StudyService;
import com.astar.gostudy_be.domain.user.dto.AccountAdapter;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(StudyController.class)
class StudyControllerTest {

    @Autowired
    private WebApplicationContext context;

    MockMvc mockMvc;

    @MockBean
    StudyService studyService;

    Account loginAccount;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        loginAccount = Account.builder()
                .id(1L)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .roles(Collections.singletonList("USER"))
                .build();
    }

    @WithMockUser
    @Test
    void studies() throws Exception {
        // given
        Long studyId = 1L;
        String name = "스터디명 1";
        String filename = "파일명 1";
        String categoryName = "카테고리 1";
        StudyType type = StudyType.OFFLINE;
        String location = "장소 1";
        Integer currentNumber = 0;
        Integer recruitmentNumber = 10;
        JoinType joinType = JoinType.FREE;
        String introduce = "소개 1";

        Category category = Category.builder()
                .id(1L)
                .name(categoryName)
                .build();
        Study study = Study.builder()
                .id(studyId)
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
                .account(loginAccount)
                .build();
        List<StudyListDto> studyListDtos = new ArrayList<>();
        studyListDtos.add(new StudyListDto(study));

        given(studyService.findAllStudies()).willReturn(studyListDtos);

        // when & then
        mockMvc.perform(get("/api/v1/studies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value(name))
                .andExpect(jsonPath("$.[0].filename").value(filename))
                .andExpect(jsonPath("$.[0].categoryName").value(categoryName))
                .andExpect(jsonPath("$.[0].type").value(type.name()))
                .andExpect(jsonPath("$.[0].location").value(location))
                .andExpect(jsonPath("$.[0].currentNumber").value(currentNumber))
                .andExpect(jsonPath("$.[0].recruitmentNumber").value(recruitmentNumber))
                .andExpect(jsonPath("$.[0].joinType").value(joinType.name()))
                .andExpect(jsonPath("$.[0].introduce").value(introduce));
    }

    @WithMockUser(roles = "USER")
    @Test
    void myStudies() throws Exception {
        // given
        Long studyId = 1L;
        String name = "스터디명 1";
        String filename = "파일명 1";
        String categoryName = "카테고리 1";
        StudyType type = StudyType.OFFLINE;
        String location = "장소 1";
        Integer currentNumber = 0;
        Integer recruitmentNumber = 10;
        JoinType joinType = JoinType.FREE;
        String introduce = "소개 1";

        Category category = Category.builder()
                .id(1L)
                .name(categoryName)
                .build();
        Study study = Study.builder()
                .id(studyId)
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
                .account(loginAccount)
                .build();
        List<StudyListDto> studyListDtos = new ArrayList<>();
        studyListDtos.add(new StudyListDto(study));

        given(studyService.findAllStudiesByAccount(eq(loginAccount))).willReturn(studyListDtos);

        // when & then
        mockMvc.perform(get("/api/v1/my-studies")
                        .with(user(new AccountAdapter(loginAccount))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value(name))
                .andExpect(jsonPath("$.[0].filename").value(filename))
                .andExpect(jsonPath("$.[0].categoryName").value(categoryName))
                .andExpect(jsonPath("$.[0].type").value(type.name()))
                .andExpect(jsonPath("$.[0].location").value(location))
                .andExpect(jsonPath("$.[0].currentNumber").value(currentNumber))
                .andExpect(jsonPath("$.[0].recruitmentNumber").value(recruitmentNumber))
                .andExpect(jsonPath("$.[0].joinType").value(joinType.name()))
                .andExpect(jsonPath("$.[0].introduce").value(introduce));
    }

    @WithMockUser(roles = "USER")
    @Test
    void create() throws Exception {
        // given
        Long studyId = 1L;
        String name = "스터디명 1";
        Long categoryId = 1L;
        StudyType type = StudyType.OFFLINE;
        String location = "장소 1";
        Long recruitmentNumber = 10L;
        Visibility visibility = Visibility.PUBLIC;
        JoinType joinType = JoinType.FREE;
        String introduce = "소개 1";

        given(studyService.createStudy(any(), eq(loginAccount))).willReturn(studyId);

        // when & then
        mockMvc.perform(post("/api/v1/studies")
                        .with(csrf())
                        .with(user(new AccountAdapter(loginAccount)))
                        .contentType("multipart/form-data")
                        .param("name", name)
                        .param("categoryId", String.valueOf(categoryId))
                        .param("type", type.name())
                        .param("location", location)
                        .param("recruitmentNumber", String.valueOf(recruitmentNumber))
                        .param("visibility", visibility.name())
                        .param("joinType", joinType.name())
                        .param("introduce", introduce))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(studyId));
    }

    @WithMockUser(roles = "USER")
    @Test
    void update() throws Exception {
        // given
        Long studyId = 1L;
        StudyUpdateDto studyUpdateDto = new StudyUpdateDto(studyId, "스터디명 1", 1L, StudyType.ONLINE, "장소 1", 5, Visibility.PUBLIC, JoinType.FREE, "소개 1");

        given(studyService.updateStudy(any(), eq(studyId), eq(loginAccount))).willReturn(studyId);

        // when & then
        mockMvc.perform(patch("/api/v1/studies/" + studyId)
                        .with(csrf())
                        .with(user(new AccountAdapter(loginAccount)))
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(studyUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(studyId));
    }

    @WithMockUser(roles = "USER")
    @Test
    void study() throws Exception {
        // given
        Long studyId = 1L;
        String name = "스터디명 1";
        String filename = "파일명 1";
        Long categoryId = 1L;
        String categoryName = "카테고리 1";
        StudyType type = StudyType.OFFLINE;
        String location = "장소 1";
        Integer currentNumber = 0;
        Integer recruitmentNumber = 10;
        Visibility visibility = Visibility.PUBLIC;
        JoinType joinType = JoinType.FREE;
        Boolean isRecruiting = true;
        String introduce = "소개 1";
        String accessUrl = "URL 1";
        String creatorEmail = "이메일 1";

        Category category = Category.builder()
                .id(categoryId)
                .name(categoryName)
                .build();
        Study study = Study.builder()
                .id(studyId)
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
                .account(loginAccount)
                .build();

        given(studyService.findStudyById(eq(studyId))).willReturn(new StudyDto(study));

        // when & then
        mockMvc.perform(get("/api/v1/studies/" + studyId)
                        .with(user(new AccountAdapter(loginAccount))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.filename").value(filename))
                .andExpect(jsonPath("$.categoryId").value(categoryId))
                .andExpect(jsonPath("$.categoryName").value(categoryName))
                .andExpect(jsonPath("$.type").value(type.name()))
                .andExpect(jsonPath("$.location").value(location))
                .andExpect(jsonPath("$.currentNumber").value(currentNumber))
                .andExpect(jsonPath("$.recruitmentNumber").value(recruitmentNumber))
                .andExpect(jsonPath("$.visibility").value(visibility.name()))
                .andExpect(jsonPath("$.joinType").value(joinType.name()))
                .andExpect(jsonPath("$.isRecruiting").value(isRecruiting))
                .andExpect(jsonPath("$.introduce").value(introduce))
                .andExpect(jsonPath("$.accessUrl").value(accessUrl))
                .andExpect(jsonPath("$.creatorEmail").value(creatorEmail));

    }

    @WithMockUser(roles = "USER")
    @Test
    void studyId() throws Exception {
        // given
        Long studyId = 1L;
        String accessUrl = "URL 1";

        given(studyService.findStudyIdByAccessUrl(eq(accessUrl))).willReturn(studyId);

        // when & then
        mockMvc.perform(get("/api/v1/access-url/" + accessUrl)
                        .with(user(new AccountAdapter(loginAccount))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(studyId));
    }

    @WithMockUser(roles = "USER")
    @Test
    void delete_() throws Exception {
        // given
        Long studyId = 1L;

        given(studyService.deleteStudy(eq(studyId), eq(loginAccount))).willReturn(studyId);

        // when & then
        mockMvc.perform(delete("/api/v1/studies/" + studyId)
                        .with(csrf())
                        .with(user(new AccountAdapter(loginAccount))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(studyId));
    }

    @WithMockUser(roles = "USER")
    @Test
    void categories() throws Exception {
        // given
        Long id = 1L;
        String name = "카테고리 1";
        Category category = Category.builder()
                .id(id)
                .name(name)
                .build();

        List<CategoryListDto> categoryListDtos = new ArrayList<>();
        categoryListDtos.add(new CategoryListDto(category));

        given(studyService.findAllCategories()).willReturn(categoryListDtos);

        // when & then
        mockMvc.perform(get("/api/v1/categories")
                        .with(user(new AccountAdapter(loginAccount))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].name").value(name));
    }

    @WithMockUser(roles = "USER")
    @Test
    void close() throws Exception {
        // given
        Long id = 1L;
        given(studyService.closeStudy(eq(id), eq(loginAccount))).willReturn(id);

        // when & then
        mockMvc.perform(post("/api/v1/studies/" + id + "/close")
                        .with(csrf())
                        .with(user(new AccountAdapter(loginAccount))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }

    @WithMockUser(roles = "USER")
    @Test
    void participate() throws Exception {
        // given
        Long studyId = 1L;
        Long id = 1L;
        String message = "메시지 1";
        class MessageDto implements Serializable {
            final String message;

            public MessageDto(String message) {
                this.message = message;
            }

            public String getMessage() {
                return message;
            }
        }
        MessageDto messageObject = new MessageDto(message);

        given(studyService.participateStudy(eq(studyId), eq(message), eq(loginAccount))).willReturn(id);

        // when & then
        mockMvc.perform(post("/api/v1/studies/" + studyId + "/participants")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(messageObject))
                        .with(csrf())
                        .with(user(new AccountAdapter(loginAccount))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }

    @WithMockUser(roles = "USER")
    @Test
    void withdraw() throws Exception {
        // given
        Long id = 1L;
        Long studyId = 1L;

        given(studyService.withdrawStudy(eq(studyId), eq(loginAccount))).willReturn(id);

        // when & then
        mockMvc.perform(post("/api/v1/studies/" + studyId + "/withdraw")
                        .with(csrf())
                        .with(user(new AccountAdapter(loginAccount))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }

    @WithMockUser(roles = "USER")
    @Test
    void cancel() throws Exception {
        // given
        Long id = 1L;
        Long studyId = 1L;

        given(studyService.cancelApplicationStudy(eq(studyId), eq(loginAccount))).willReturn(id);

        // when & then
        mockMvc.perform(post("/api/v1/studies/" + studyId + "/cancel")
                        .with(csrf())
                        .with(user(new AccountAdapter(loginAccount))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }

    @Test
    void showStudyImage() throws Exception {
        // given
        String filename = "default.jpg";

        // when & then
        mockMvc.perform(get("/images/study/" + filename)
                .with(user(new AccountAdapter(loginAccount))))
                .andExpect(status().isOk());
    }
}