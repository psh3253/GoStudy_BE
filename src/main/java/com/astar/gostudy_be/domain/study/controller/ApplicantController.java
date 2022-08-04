package com.astar.gostudy_be.domain.study.controller;

import com.astar.gostudy_be.domain.study.dto.ApplicantListDto;
import com.astar.gostudy_be.domain.study.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ApplicantController {
    private final ApplicantService applicantService;

    @GetMapping("/api/v1/studies/{id}/applicants")
    public List<ApplicantListDto> applicants(@PathVariable Long id)  {
        return applicantService.findAllApplicantsByStudyId(id);
    }
}
