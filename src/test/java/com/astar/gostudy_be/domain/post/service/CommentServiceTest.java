package com.astar.gostudy_be.domain.post.service;

import com.astar.gostudy_be.domain.post.dto.CommentListDto;
import com.astar.gostudy_be.domain.post.entity.Comment;
import com.astar.gostudy_be.domain.post.entity.Post;
import com.astar.gostudy_be.domain.post.repository.CommentRepository;
import com.astar.gostudy_be.domain.post.repository.PostRepository;
import com.astar.gostudy_be.domain.study.entity.*;
import com.astar.gostudy_be.domain.study.repository.ParticipantRepository;
import com.astar.gostudy_be.domain.user.entity.Account;
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
class CommentServiceTest {

    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    PostRepository postRepository;

    @Mock
    ParticipantRepository participantRepository;

    @Test
    void findAllCommentsByPostId() {
        // given
        Long id = 1L;
        Long postId = 1L;
        String content = "내용 1";
        String email = "이메일 1";
        String image = "이미지 1";
        String nickname = "닉네임 1";

        Account account = Account.builder()
                .id(1L)
                .email(email)
                .password("비밀번호 1")
                .nickname(nickname)
                .image(image)
                .introduce("소개 1")
                .refreshToken("리프레쉬 토큰 1")
                .build();
        Category category = Category.builder()
                .id(1L)
                .name("카테고리 1")
                .build();
        Study study = Study.builder()
                .id(1L)
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
        Post post = Post.builder()
                .id(id)
                .title("제목 1")
                .content("내용 1")
                .image(image)
                .account(account)
                .study(study)
                .commentCount(0L)
                .build();
        Comment comment = Comment.builder()
                .id(id)
                .content(content)
                .post(post)
                .account(account)
                .build();
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        when(commentRepository.findAllByPostId(postId)).thenReturn(comments);

        // when
        List<CommentListDto> commentListDtos = commentService.findAllCommentsByPostId(postId);

        // then
        assertThat(commentListDtos.get(0).getId()).isEqualTo(id);
        assertThat(commentListDtos.get(0).getContent()).isEqualTo(content);
        assertThat(commentListDtos.get(0).getCreatorEmail()).isEqualTo(email);
        assertThat(commentListDtos.get(0).getCreatorImage()).isEqualTo(image);
        assertThat(commentListDtos.get(0).getCreatorNickname()).isEqualTo(nickname);
    }

    @Test
    void createComment() {
        // given
        Long id = 1L;
        String content = "내용 1";
        Long postId = 1L;
        Long studyId = 1L;
        String email = "이메일 1";

        Account account = Account.builder()
                .id(1L)
                .email(email)
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
        Post post = Post.builder()
                .id(postId)
                .title("제목 1")
                .content("내용 1")
                .image("이미지 1")
                .account(account)
                .study(study)
                .commentCount(0L)
                .build();
        Participant participant = Participant.builder()
                .id(1L)
                .account(account)
                .study(study)
                .build();
        Comment comment = Comment.builder()
                .id(id)
                .content(content)
                .post(post)
                .account(account)
                .build();


        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        when(participantRepository.findByStudyIdAndAccountEmail(studyId, email)).thenReturn(Optional.ofNullable(participant));

        // when
        Long commentId = commentService.createComment(content, postId, account);

        // then
        assertThat(commentId).isEqualTo(id);
    }

    @Test
    void deleteComment() {
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
                .id(1L)
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
        Post post = Post.builder()
                .id(1L)
                .title("제목 1")
                .content("내용 1")
                .image("이미지 1")
                .account(account)
                .study(study)
                .commentCount(0L)
                .build();
        Comment comment = Comment.builder()
                .id(id)
                .content("내용 1")
                .post(post)
                .account(account)
                .build();

        when(commentRepository.findById(id)).thenReturn(Optional.ofNullable(comment));

        // when
        Long commentId = commentService.deleteComment(id, account);

        // then
        assertThat(commentId).isEqualTo(id);
    }
}