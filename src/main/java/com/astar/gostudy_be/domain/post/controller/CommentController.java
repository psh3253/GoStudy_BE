package com.astar.gostudy_be.domain.post.controller;

import com.astar.gostudy_be.annotation.CurrentUser;
import com.astar.gostudy_be.domain.post.dto.CommentListDto;
import com.astar.gostudy_be.domain.post.service.CommentService;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/v1/studies/{sid}/posts/{pid}/comments")
    public List<CommentListDto> comments(@PathVariable Long sid, @PathVariable Long pid) {
        return commentService.findAllCommentsByPostId(pid);
    }

    @PostMapping("/api/v1/studies/{sid}/posts/{pid}/comments")
    public Long create(@RequestBody Map<String, String> commentCreateDto, @PathVariable Long sid, @PathVariable Long pid, @CurrentUser Account account) {
        return commentService.createComment(commentCreateDto.get("content"), pid, account);
    }

    @DeleteMapping("/api/v1/studies/{sid}/posts/{pid}/comments/{cid}")
    public Long delete(@PathVariable Long sid, @PathVariable Long pid, @PathVariable Long cid, @CurrentUser Account account) {
        return commentService.deleteComment(cid, account);
    }
}
