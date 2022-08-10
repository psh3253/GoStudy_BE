package com.astar.gostudy_be.domain.post.dto;

import com.astar.gostudy_be.domain.post.entity.Post;
import com.astar.gostudy_be.domain.study.entity.Study;
import com.astar.gostudy_be.domain.user.entity.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateDto {
    private String title;

    private String content;

    @JsonIgnore
    private MultipartFile image;

    public Post toEntity(Study study, Account account, String filename) {
        return Post.builder()
                .account(account)
                .study(study)
                .title(title)
                .content(content)
                .image(filename)
                .build();
    }
}
