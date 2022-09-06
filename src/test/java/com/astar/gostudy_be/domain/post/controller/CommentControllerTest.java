package com.astar.gostudy_be.domain.post.controller;

import com.astar.gostudy_be.domain.post.dto.CommentListDto;
import com.astar.gostudy_be.domain.post.entity.Comment;
import com.astar.gostudy_be.domain.post.entity.Post;
import com.astar.gostudy_be.domain.post.service.CommentService;
import com.astar.gostudy_be.domain.post.service.PostService;
import com.astar.gostudy_be.domain.study.controller.ApplicantController;
import com.astar.gostudy_be.domain.study.entity.*;
import com.astar.gostudy_be.domain.user.dto.AccountAdapter;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private WebApplicationContext context;

    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    Account account = null;
    Category category = null;
    Study study = null;
    Post post = null;
    Comment comment = null;

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
        comment = Comment.builder()
                .content("내용 1")
                .post(post)
                .account(account)
                .build();

    }

    @Test
    @DisplayName("특정 게시글의 모든 댓글 조회")
    void comments() throws Exception {
        // given
        Long studyId = 1L;
        Long postId = 1L;
        Long id = 1L;
        String content = "내용 1";
        String creatorEmail = "이메일 1";
        String creatorNickname = "닉네임 1";
        String creatorImage = "이미지 1";

        List<CommentListDto> commentListDtos= new ArrayList<>();
        ReflectionTestUtils.setField(comment, "id", id);
        commentListDtos.add(new CommentListDto(comment));

        given(commentService.findAllCommentsByPostId(eq(postId))).willReturn(commentListDtos);

        // when & then
        mockMvc.perform(get("/api/v1/studies/" + studyId + "/posts/" + postId + "/comments")
                .with(user(new AccountAdapter(account))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id))
                .andExpect(jsonPath("$.[0].content").value(content))
                .andExpect(jsonPath("$.[0].creatorEmail").value(creatorEmail))
                .andExpect(jsonPath("$.[0].creatorNickname").value(creatorNickname))
                .andExpect(jsonPath("$.[0].creatorImage").value(creatorImage));
    }

    @Test
    @DisplayName("댓글 생성")
    void create() throws Exception {
        // given
        Long studyId = 1L;
        Long postId = 1L;
        Long id = 1L;
        String content = "내용 1";

        class CommentCreateDto implements Serializable {
            final String content;

            public CommentCreateDto(String content) {
                this.content = content;
            }

            public String getContent() {
                return content;
            }
        }
        CommentCreateDto commentCreateDto = new CommentCreateDto(content);

        given(commentService.createComment(eq(content), eq(postId), eq(account))).willReturn(id);

        // when & then
        mockMvc.perform(post("/api/v1/studies/" + studyId + "/posts/" + postId + "/comments/")
                        .with(csrf())
                        .with(user(new AccountAdapter(account)))
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(commentCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }

    @Test
    @DisplayName("댓글 삭제")
    void _delete() throws Exception {
        // given
        Long studyId = 1L;
        Long postId = 1L;
        Long id = 1L;

        given(commentService.deleteComment(eq(id), eq(account))).willReturn(id);

        // when & then
        mockMvc.perform(delete("/api/v1/studies/" + studyId + "/posts/" + postId + "/comments/" + id)
                        .with(csrf())
                        .with(user(new AccountAdapter(account)))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(id));
    }
}