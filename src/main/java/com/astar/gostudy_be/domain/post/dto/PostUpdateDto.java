package com.astar.gostudy_be.domain.post.dto;

import com.astar.gostudy_be.domain.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostUpdateDto {

    private Long id;

    private String title;

    private String content;

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}
