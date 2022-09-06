package com.astar.gostudy_be.domain.post.controller;

import com.astar.gostudy_be.GoStudyBeApplication;
import com.astar.gostudy_be.domain.post.dto.PostCreateDto;
import com.astar.gostudy_be.domain.post.dto.PostDto;
import com.astar.gostudy_be.domain.post.dto.PostListDto;
import com.astar.gostudy_be.domain.post.dto.PostUpdateDto;
import com.astar.gostudy_be.domain.post.entity.Post;
import com.astar.gostudy_be.domain.post.service.PostService;
import com.astar.gostudy_be.domain.study.dto.StudyListDto;
import com.astar.gostudy_be.domain.study.entity.*;
import com.astar.gostudy_be.domain.study.service.ApplicantService;
import com.astar.gostudy_be.domain.user.dto.AccountAdapter;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private WebApplicationContext context;

    MockMvc mockMvc;

    @MockBean
    PostService postService;

    Account account = null;
    Category category = null;
    Study study = null;
    Post post = null;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        account = Account.builder()
                .id(1L)
                .email("이메일 1")
                .password("비밀번호 1")
                .nickname("닉네임 1")
                .image("이미지 1")
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .roles(Collections.singletonList("USER"))
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
        post = Post.builder()
                .title("제목 1")
                .content("내용 1")
                .image("이미지 1")
                .commentCount(0L)
                .study(study)
                .account(account)
                .build();
    }

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("특정 스터디의 모든 게시글 조회")
    void posts() throws Exception {
        // given
        Long studyId = 1L;
        Long id = 1L;
        String title = "제목 1";
        String image = "이미지 1";
        Long commentCount = 0L;
        String creatorEmail = "이메일 1";
        String creatorNickname = "닉네임 1";
        String creatorImage = "이미지 1";

        List<PostListDto> postListDtos = new ArrayList<>();
        ReflectionTestUtils.setField(post, "id", id);
        postListDtos.add(new PostListDto(post));

        given(postService.findAllPostsByStudyId(eq(id))).willReturn(postListDtos);

        // when & then
        mockMvc.perform(get("/api/v1/studies/" + studyId + "/posts")
                .with(user(new AccountAdapter(account))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].title").value(title))
                .andExpect(jsonPath("$.[0].image").value(image))
                .andExpect(jsonPath("$.[0].commentCount").value(commentCount))
                .andExpect(jsonPath("$.[0].creatorEmail").value(creatorEmail))
                .andExpect(jsonPath("$.[0].creatorNickname").value(creatorNickname))
                .andExpect(jsonPath("$.[0].creatorImage").value(creatorImage));
    }

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("특정 게시글 조회")
    void _post() throws Exception {
        // given
        Long studyId = 1L;
        Long id = 1L;
        String title = "제목 1";
        String content = "내용 1";
        String image = "이미지 1";
        Long commentCount = 0L;
        String creatorEmail = "이메일 1";
        String creatorNickname = "닉네임 1";
        String creatorImage = "이미지 1";

        ReflectionTestUtils.setField(post, "id", id);
        PostDto postDto = new PostDto(post);

        given(postService.findPostByPostId(eq(id))).willReturn(postDto);

        // when & then
        mockMvc.perform(get("/api/v1/studies/" + studyId + "/posts/" + id)
                .with(user(new AccountAdapter(account))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.image").value(image))
                .andExpect(jsonPath("$.commentCount").value(commentCount))
                .andExpect(jsonPath("$.creatorEmail").value(creatorEmail))
                .andExpect(jsonPath("$.creatorNickname").value(creatorNickname))
                .andExpect(jsonPath("$.creatorImage").value(creatorImage));


    }

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("게시글 생성")
    void create() throws Exception {
        // given
        Long id = 1L;
        Long studyId = 1L;
        String title = "제목 1";
        String content = "내용 1";

        given(postService.createPost(any(), eq(studyId), eq(account))).willReturn(id);

        // when & then
        mockMvc.perform(post("/api/v1/studies/" + studyId + "/posts")
                .with(csrf())
                .with(user(new AccountAdapter(account)))
                .contentType("multipart/form-data")
                .param("title", title)
                .param("content", content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("게시글 수정")
    void update() throws Exception {
        // given
        Long studyId = 1L;
        Long id = 1L;
        String title = "제목 1";
        String content = "내용 1";

        PostUpdateDto postUpdateDto = new PostUpdateDto(id, title, content);

        given(postService.updatePost(any(), eq(id), eq(account))).willReturn(id);

        // when & then
        mockMvc.perform(patch("/api/v1/studies/" + studyId + "/posts/" + id)
                        .with(csrf())
                        .with(user(new AccountAdapter(account)))
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(postUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }

    @WithMockUser(roles = "USER")
    @Test
    @DisplayName("게시글 삭제")
    void _delete() throws Exception {
        // given
        Long studyId = 1L;
        Long id = 1L;

        given(postService.deletePost(eq(id), eq(account))).willReturn(id);

        // when & then
        mockMvc.perform(delete("/api/v1/studies/" + studyId + "/posts/" + id)
                        .with(csrf())
                        .with(user(new AccountAdapter(account)))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }
}