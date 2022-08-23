package com.astar.gostudy_be.domain.study.controller;

import com.astar.gostudy_be.annotation.CurrentUser;
import com.astar.gostudy_be.config.Config;
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
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StudyController {

    private final StudyService studyService;

    @GetMapping("/api/v1/studies")
    public List<StudyListDto> studies() {
        return studyService.findAllStudies();
    }

    @GetMapping("/api/v1/my-studies")
    public List<StudyListDto> myStudies(@CurrentUser Account account) {
        return studyService.findAllStudiesByAccount(account);
    }

    @PostMapping("/api/v1/studies")
    public Long create(@ModelAttribute StudyCreateDto studyCreateDto, @CurrentUser Account account) {
        log.info(account.getEmail());
        return studyService.createStudy(studyCreateDto, account);
    }

    @PatchMapping("/api/v1/studies/{id}")
    public Long update(@RequestBody StudyUpdateDto studyUpdateDto, @PathVariable Long id, @CurrentUser Account account) {
        return studyService.updateStudy(studyUpdateDto, id, account);
    }

    @GetMapping("/api/v1/studies/{id}")
    public StudyDto study(@PathVariable Long id) {
        return studyService.findStudyById(id);
    }

    @GetMapping("/api/v1/access-url/{accessUrl}")
    public Long studyId(@PathVariable String accessUrl) {
        return studyService.findStudyIdByAccessUrl(accessUrl);
    }

    @DeleteMapping("/api/v1/studies/{id}")
    public Long delete(@PathVariable Long id, @CurrentUser Account account) {
        return studyService.deleteStudy(id, account);
    }

    @GetMapping("/api/v1/categories")
    public List<CategoryListDto> categories() {
        return studyService.findAllCategories();
    }

    @PostMapping("/api/v1/studies/{id}/close")
    public Long close(@PathVariable Long id, @CurrentUser Account account) {
        return studyService.closeStudy(id, account);
    }

    @PostMapping("/api/v1/studies/{id}/participants")
    public Long participate(@PathVariable Long id, @RequestBody Map<String, String> data, @CurrentUser Account account) {
        return studyService.participateStudy(id, data.get("message"), account);
    }

    @PostMapping("/api/v1/studies/{id}/withdraw")
    public Long withdraw(@PathVariable Long id, @CurrentUser Account account) {
        return studyService.withdrawStudy(id, account);
    }

    @PostMapping("/api/v1/studies/{id}/cancel")
    public Long cancel(@PathVariable Long id, @CurrentUser Account account) {
        return studyService.cancelApplicationStudy(id, account);
    }

    @ResponseBody
    @GetMapping("/images/study/{filename}")
    public Resource showStudyImage(@PathVariable String filename) throws MalformedURLException {
        File imageFile = new File(Config.UPLOAD_FILE_PATH + "uploads/study/thumbnail_images/thumbnail_" + filename);
        return new UrlResource("file:" + imageFile.getAbsolutePath());
    }
}
