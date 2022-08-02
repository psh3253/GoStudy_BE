package com.astar.gostudy_be.domain.study.controller;

import com.astar.gostudy_be.annotation.CurrentUser;
import com.astar.gostudy_be.domain.study.dto.*;
import com.astar.gostudy_be.domain.study.service.StudyService;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StudyController {

    private final StudyService studyService;

    @GetMapping("/api/v1/studies")
    public List<StudyListDto> studies(@RequestParam(value = "category-id", required = false, defaultValue = "0") Long categoryId) {
        if (categoryId == 0)
            return studyService.findAllStudies();
        else
            return studyService.findAllStudiesByCategoryId(categoryId);
    }

    @PostMapping("/api/v1/studies")
    public Long create(@ModelAttribute StudyCreateDto studyCreateDto, @CurrentUser Account account) {
        return studyService.createStudy(studyCreateDto, account);
    }

    @PatchMapping("/api/v1/studies/{id}")
    public Long update(@RequestBody StudyUpdateDto studyUpdateDto, @PathVariable Long id, @CurrentUser Account account) {
        return studyService.updateStudy(studyUpdateDto, id, account);
    }

    @GetMapping("/api/v1/studies/{id}")
    public StudyDto study(@PathVariable Long id) {
        return studyService.findStudy(id);
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

    @PostMapping("/api/v1/studies/{id}/close")
    public Long close(@PathVariable Long id, @CurrentUser Account account) {
        return studyService.closeStudy(id, account);
    }

    @ResponseBody
    @GetMapping("/images/study/{filename}")
    public Resource showStudyImage(@PathVariable String filename) throws MalformedURLException {
        File imageFile = new File("C://uploads/thumbnail_image/" + filename);
        return new UrlResource("file:" + imageFile.getAbsolutePath());
    }

    @ResponseBody
    @GetMapping("/images/user-default-image")
    public Resource showUserDefaultImage() throws MalformedURLException {
        File imageFile = new File("C://uploads/user.png");
        return new UrlResource("file:" + imageFile.getAbsolutePath());
    }
}
