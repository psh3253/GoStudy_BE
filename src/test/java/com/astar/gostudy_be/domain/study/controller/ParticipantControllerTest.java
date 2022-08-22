package com.astar.gostudy_be.domain.study.controller;

import com.astar.gostudy_be.domain.study.dto.ParticipantListDto;
import com.astar.gostudy_be.domain.study.entity.*;
import com.astar.gostudy_be.domain.study.service.ParticipantService;
import com.astar.gostudy_be.domain.user.dto.AccountAdapter;
import com.astar.gostudy_be.domain.user.entity.Account;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ParticipantController.class)
class ParticipantControllerTest {

    @Autowired
    private WebApplicationContext context;

    MockMvc mockMvc;

    @MockBean
    ParticipantService participantService;

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

    @WithMockUser(roles = "USER")
    @Test
    void participants() throws Exception {
        // given
        Long id = 1L;
        String email = "이메일 1";
        String nickname = "닉네임 1";
        String image = "이미지 1";
        String introduce = "소개 1";
        Long studyId = 1L;

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
                .account(loginAccount)
                .build();

        Participant participant = Participant.builder()
                .id(1L)
                .study(study)
                .account(loginAccount)
                .build();

        List<ParticipantListDto> participantListDtos = new ArrayList<>();
        participantListDtos.add(new ParticipantListDto(participant));

        given(participantService.findAllParticipantsByStudyId(eq(studyId))).willReturn(participantListDtos);

        // when & then
        mockMvc.perform(get("/api/v1/studies/" + studyId + "/participants")
                .with(user(new AccountAdapter(loginAccount))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].email").value(email))
                .andExpect(jsonPath("$.[0].nickname").value(nickname))
                .andExpect(jsonPath("$.[0].image").value(image))
                .andExpect(jsonPath("$.[0].introduce").value(introduce));
    }

    @WithMockUser(roles = "USER")
    @Test
    void _delete() throws Exception {
        // given
        Long id = 1L;
        Long studyId = 1L;

        given(participantService.deleteParticipant(eq(id), eq(loginAccount))).willReturn(id);

        // when & then
        mockMvc.perform(delete("/api/v1/studies/" + studyId + "/participants/" + id)
                        .with(csrf())
                        .with(user(new AccountAdapter(loginAccount))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }
}