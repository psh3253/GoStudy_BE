package com.astar.gostudy_be.domain.post.repository;

import com.astar.gostudy_be.domain.post.entity.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Test
    @DisplayName("특정 게시글의 모든 댓글 조회")
    void findAllByPostId() {
        // 게시글이 존재하는 경우
        {
            // given
            String content = "내용 1";
            Long postId = 1L;
            String email = "이메일 1";

            // when
            Comment comment = commentRepository.findAllByPostId(postId).get(0);

            // then
            assertThat(comment.getContent()).isEqualTo(content);
            assertThat(comment.getPost().getId()).isEqualTo(postId);
            assertThat(comment.getAccount().getEmail()).isEqualTo(email);
        }
        // 게시글이 존재하지 않는 경우
        {
            // given
            Long postId = -1L;

            // when
            Integer count = commentRepository.findAllByPostId(postId).size();

            // then
            assertThat(count).isEqualTo(0);
        }
    }
}