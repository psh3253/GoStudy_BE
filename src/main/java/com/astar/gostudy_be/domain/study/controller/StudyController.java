package com.astar.gostudy_be.domain.study.controller;

import com.astar.gostudy_be.annotation.CurrentUser;
import com.astar.gostudy_be.domain.study.dto.CategoryListDto;
import com.astar.gostudy_be.domain.study.dto.StudyCreateDto;
import com.astar.gostudy_be.domain.study.dto.StudyListDto;
import com.astar.gostudy_be.domain.study.dto.StudyUpdateDto;
import com.astar.gostudy_be.domain.study.service.StudyService;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StudyController {

    private final StudyService studyService;

    @GetMapping("/api/v1")
    public String index(@CurrentUser Account account) {
        log.info(account.getEmail());
        return "index";
    }

    @GetMapping("/api/v1/studies")
    public List<StudyListDto> studies(@RequestParam(value = "category-id", required = false, defaultValue = "0") Long categoryId) {
        if (categoryId == 0)
            return studyService.findAllStudies();
        else
            return studyService.findAllStudiesByCategoryId(categoryId);
    }

    @PostMapping("/api/v1/studies")
    public Long create(@RequestBody StudyCreateDto studyCreateDto, @CurrentUser Account account) {
        return studyService.createStudy(studyCreateDto, account);
    }

    @PatchMapping("/api/v1/studies/{id}")
    public Long update(@RequestBody StudyUpdateDto studyUpdateDto, @PathVariable Long id, @CurrentUser Account account) {
        return studyService.updateStudy(studyUpdateDto, id, account);
    }

    @DeleteMapping("/api/v1/studies/{id}")
    public Long delete(@PathVariable Long id, @CurrentUser Account account) {
        studyService.deleteStudy(id, account);
        return id;
    }

    @GetMapping("/api/v1/categories")
    public List<CategoryListDto> categories() {
        return studyService.findAllCategories();
    }
}
