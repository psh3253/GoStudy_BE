package com.astar.gostudy_be.domain.post.service;

import com.astar.gostudy_be.domain.post.dto.PostCreateDto;
import com.astar.gostudy_be.domain.post.dto.PostDto;
import com.astar.gostudy_be.domain.post.dto.PostListDto;
import com.astar.gostudy_be.domain.post.dto.PostUpdateDto;
import com.astar.gostudy_be.domain.post.entity.Post;
import com.astar.gostudy_be.domain.post.repository.PostRepository;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    @Mock
    StudyRepository studyRepository;

    @Mock
    ParticipantRepository participantRepository;

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
    }

    @Test
    @DisplayName("특정 스터디의 모든 게시글 조회")
    void findAllPostsByStudyId() {
        // given
        Long studyId = 1L;
        Long id = 1L;
        String title = "제목 1";
        String image = "이미지 1";
        Long commentCount = 0L;
        String creatorEmail = "이메일 1";
        String creatorNickname = "닉네임 1";
        String creatorImage = "이미지 1";

        ReflectionTestUtils.setField(study, "id", studyId);
        Post post = Post.builder()
                .title(title)
                .content("내용 1")
                .image(image)
                .account(account)
                .study(study)
                .commentCount(commentCount)
                .build();
        ReflectionTestUtils.setField(post, "id", id);
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        when(postRepository.findAllByStudyId(studyId)).thenReturn(posts);

        // when
        List<PostListDto> postListDtos = postService.findAllPostsByStudyId(studyId);

        // then
        assertThat(postListDtos.get(0).getId()).isEqualTo(id);
        assertThat(postListDtos.get(0).getTitle()).isEqualTo(title);
        assertThat(postListDtos.get(0).getImage()).isEqualTo(image);
        assertThat(postListDtos.get(0).getCommentCount()).isEqualTo(commentCount);
        assertThat(postListDtos.get(0).getCreatorEmail()).isEqualTo(creatorEmail);
        assertThat(postListDtos.get(0).getCreatorImage()).isEqualTo(creatorImage);
        assertThat(postListDtos.get(0).getCreatorNickname()).isEqualTo(creatorNickname);
    }

    @Test
    @DisplayName("특정 게시글 조회")
    void findPostByPostId() {
        // given
        Long id = 1L;
        Long studyId = 1L;
        String title = "제목 1";
        String content = "내용 1";
        String image = "이미지 1";
        Long commentCount = 0L;
        String creatorEmail = "이메일 1";
        String creatorNickname = "닉네임 1";
        String creatorImage = "이미지 1";

        ReflectionTestUtils.setField(study, "id", studyId);
        Post post = Post.builder()
                .title(title)
                .content("내용 1")
                .image(image)
                .account(account)
                .study(study)
                .commentCount(commentCount)
                .build();
        ReflectionTestUtils.setField(post, "id", id);

        when(postRepository.findById(id)).thenReturn(Optional.ofNullable(post));

        // when
        PostDto postDto = postService.findPostByPostId(id);

        // then
        assertThat(postDto.getId()).isEqualTo(id);
        assertThat(postDto.getTitle()).isEqualTo(title);
        assertThat(postDto.getContent()).isEqualTo(content);
        assertThat(postDto.getImage()).isEqualTo(image);
        assertThat(postDto.getCommentCount()).isEqualTo(commentCount);
        assertThat(postDto.getCreatorEmail()).isEqualTo(creatorEmail);
        assertThat(postDto.getCreatorImage()).isEqualTo(creatorImage);
        assertThat(postDto.getCreatorNickname()).isEqualTo(creatorNickname);
    }

    @Test
    @DisplayName("게시글 생성")
    void createPost() {
        // given
        Long id = 1L;
        Long studyId = 1L;
        String email = "이메일 1";
        PostCreateDto postCreateDto = new PostCreateDto("제목 1", "내용 1", null);

        ReflectionTestUtils.setField(study, "id", studyId);
        Participant participant = Participant.builder()
                .account(account)
                .study(study)
                .build();
        ReflectionTestUtils.setField(participant, "id", 1L);
        Post post = postCreateDto.toEntity(study, account, "이미지 1");
        ReflectionTestUtils.setField(post, "id", id);

        when(studyRepository.findById(studyId)).thenReturn(Optional.ofNullable(study));
        when(participantRepository.findByStudyIdAndAccountEmail(studyId, email)).thenReturn(Optional.ofNullable(participant));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // when
        Long postId = postService.createPost(postCreateDto, studyId, account);

        // then
        assertThat(postId).isEqualTo(id);
    }

    @Test
    @DisplayName("게시글 수정")
    void updatePost() {
        // given
        Long id = 1L;
        Long studyId = 1L;

        PostUpdateDto postUpdateDto = new PostUpdateDto(id, "제목 2", "내용 2");
        ReflectionTestUtils.setField(study, "id", studyId);
        Post post = Post.builder()
                .title("제목 1")
                .content("내용 1")
                .image("이미지 1")
                .account(account)
                .study(study)
                .commentCount(0L)
                .build();
        ReflectionTestUtils.setField(post, "id", id);

        when(postRepository.findById(id)).thenReturn(Optional.ofNullable(post));
        when(postRepository.save(any(Post.class))).thenReturn(post.update(postUpdateDto.toEntity()));

        // when
        Long postId = postService.updatePost(postUpdateDto, id, account);

        // then
        assertThat(postId).isEqualTo(id);
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() {
        // given
        Long id = 1L;
        Long studyId = 1L;

        ReflectionTestUtils.setField(study, "id", studyId);
        Post post = Post.builder()
                .title("제목 1")
                .content("내용 1")
                .image("이미지 1")
                .account(account)
                .study(study)
                .commentCount(0L)
                .build();
        ReflectionTestUtils.setField(post, "id", id);

        when(postRepository.findById(id)).thenReturn(Optional.ofNullable(post));

        // when
        Long postId = postService.deletePost(id, account);

        // then
        assertThat(postId).isEqualTo(id);
    }
}