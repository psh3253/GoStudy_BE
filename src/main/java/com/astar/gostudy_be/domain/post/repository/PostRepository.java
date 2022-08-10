package com.astar.gostudy_be.domain.post.repository;

import com.astar.gostudy_be.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByStudyId(Long studyId);
}
