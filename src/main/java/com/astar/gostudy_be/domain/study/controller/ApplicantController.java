package com.astar.gostudy_be.domain.study.controller;

import com.astar.gostudy_be.annotation.CurrentUser;
import com.astar.gostudy_be.domain.study.dto.ApplicantListDto;
import com.astar.gostudy_be.domain.study.service.ApplicantService;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ApplicantController {
    private final ApplicantService applicantService;

    @GetMapping("/api/v1/studies/{id}/applicants")
    public List<ApplicantListDto> applicants(@PathVariable Long id)  {
        return applicantService.findAllApplicantsByStudyId(id);
    }

    @PostMapping("/api/v1/applicants/{id}")
    public Long accept(@PathVariable Long id, @CurrentUser Account account) {
        return applicantService.acceptParticipation(id, account);
    }

    @DeleteMapping("/api/v1/applicants/{id}")
    public Long delete(@PathVariable Long id, @CurrentUser Account account) {
        return applicantService.deleteApplicant(id, account);
    }
}
