package com.astar.gostudy_be.domain.post.service;

import com.astar.gostudy_be.domain.post.dto.CommentListDto;
import com.astar.gostudy_be.domain.post.entity.Comment;
import com.astar.gostudy_be.domain.post.entity.Post;
import com.astar.gostudy_be.domain.post.repository.CommentRepository;
import com.astar.gostudy_be.domain.post.repository.PostRepository;
import com.astar.gostudy_be.domain.study.entity.Participant;
import com.astar.gostudy_be.domain.study.repository.ParticipantRepository;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final ParticipantRepository participantRepository;

    @Transactional(readOnly = true)
    public List<CommentListDto> findAllCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId).stream()
                .map(CommentListDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createComment(String content, Long postId, Account account) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Participant participant = participantRepository.findByStudyIdAndAccountEmail(post.getStudy().getId(), account.getEmail()).orElseThrow(() -> new IllegalArgumentException("사용자가 스터디에 소속되어 있지 않습니다."));
        postRepository.save(post.update(Post.builder().commentCount(post.getCommentCount() + 1).build()));
        return commentRepository.save(Comment.builder()
                .content(content)
                .post(post)
                .account(account)
                .build()).getId();
    }

    @Transactional
    public Long deleteComment(Long commentId, Account account) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if(!Objects.equals(comment.getAccount().getEmail(), account.getEmail())) {
            throw new RuntimeException("사용자가 댓글의 소유자랑 일치하지 않습니다.");
        }
        postRepository.save(comment.getPost().update(Post.builder().commentCount(comment.getPost().getCommentCount() - 1).build()));
        commentRepository.delete(comment);
        return comment.getId();
    }
}
