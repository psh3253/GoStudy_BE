package com.astar.gostudy_be.domain.post.dto;

import com.astar.gostudy_be.domain.post.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentListDto {
    private Long id;

    private String content;

    private String creatorEmail;

    private String creatorNickname;

    private String creatorImage;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public CommentListDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.creatorEmail = comment.getAccount().getEmail();
        this.creatorNickname = comment.getAccount().getNickname();
        this.creatorImage = comment.getAccount().getImage();
        this.createdAt = comment.getCreatedAt();
    }
}
