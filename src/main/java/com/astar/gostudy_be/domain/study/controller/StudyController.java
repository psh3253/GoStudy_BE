package com.astar.gostudy_be.domain.study.controller;

import com.astar.gostudy_be.annotation.CurrentUser;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StudyController {

    @GetMapping("/api/v1")
    public String index(@CurrentUser Account account) {
        log.info(account.getEmail());
        return "index";
    }
}
