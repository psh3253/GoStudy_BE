package com.astar.gostudy_be.domain.post.dto;

import com.astar.gostudy_be.domain.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostListDto {

    private Long id;

    private String title;

    private String image;

    private Long commentCount;

    private String creatorEmail;

    private String creatorNickname;

    private String creatorImage;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public PostListDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.image = post.getImage();
        this.commentCount = post.getCommentCount();
        this.creatorEmail = post.getAccount().getEmail();
        this.creatorNickname = post.getAccount().getNickname();
        this.creatorImage = post.getAccount().getImage();
        this.createdAt = post.getCreatedAt();
    }
}
