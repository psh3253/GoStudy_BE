package com.astar.gostudy_be.domain.post.repository;

import com.astar.gostudy_be.domain.post.entity.Post;
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
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("특정 스터디의 모든 게시글 조회")
    void findAllByStudyId() {
        // 스터디가 존재하는 경우
        {
            // given
            String title = "제목 1";
            String content = "내용 1";
            String image = "이미지 1";
            String email = "이메일 1";
            Long commentCount = 0L;
            Long studyId = 1L;

            // when
            Post post = postRepository.findAllByStudyId(studyId).get(0);

            // then
            assertThat(post.getTitle()).isEqualTo(title);
            assertThat(post.getContent()).isEqualTo(content);
            assertThat(post.getImage()).isEqualTo(image);
            assertThat(post.getCommentCount()).isEqualTo(commentCount);
            assertThat(post.getAccount().getEmail()).isEqualTo(email);
            assertThat(post.getStudy().getId()).isEqualTo(studyId);
        }
        // 스터디가 존재하지 않는 경우
        {
            // given
            Long studyId = -1L;

            // when
            Integer count = postRepository.findAllByStudyId(studyId).size();

            // then
            assertThat(count).isEqualTo(0);
        }
    }
}