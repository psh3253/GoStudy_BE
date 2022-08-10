package com.astar.gostudy_be.domain.post.controller;

import com.astar.gostudy_be.annotation.CurrentUser;
import com.astar.gostudy_be.domain.post.dto.PostCreateDto;
import com.astar.gostudy_be.domain.post.dto.PostDto;
import com.astar.gostudy_be.domain.post.dto.PostListDto;
import com.astar.gostudy_be.domain.post.service.PostService;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/v1/studies/{id}/posts")
    public List<PostListDto> posts(@PathVariable Long id) {
        return postService.findAllPostsByStudyId(id);
    }

    @GetMapping("/api/v1/studies/{sid}/posts/{pid}")
    public PostDto post(@PathVariable Long sid, @PathVariable Long pid) {
        return postService.findPostByPostId(pid);
    }

    @PostMapping("/api/v1/studies/{id}/posts")
    public Long create(@ModelAttribute PostCreateDto postCreateDto, @PathVariable Long id, @CurrentUser Account account) {
        return postService.createPost(postCreateDto, id, account);
    }

    @DeleteMapping("/api/v1/studies/{sid}/posts/{pid}")
    public Long delete(@PathVariable Long sid, @PathVariable Long pid, @CurrentUser Account account) {
        return postService.deletePost(pid, account);
    }

    @ResponseBody
    @GetMapping("/images/post/{filename}")
    public Resource showStudyImage(@PathVariable String filename) throws MalformedURLException {
        File imageFile = new File("C://uploads/post/thumbnail_images/thumbnail_" + filename);
        return new UrlResource("file:" + imageFile.getAbsolutePath());
    }
}
