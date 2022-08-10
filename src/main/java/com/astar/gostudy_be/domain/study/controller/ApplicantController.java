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

    @PostMapping("/api/v1/studies/{sid}/applicants/{aid}")
    public Long accept(@PathVariable Long sid, @PathVariable Long aid, @CurrentUser Account account) {
        return applicantService.acceptParticipation(aid, account);
    }

    @DeleteMapping("/api/v1/studies/{sid}/applicants/{aid}")
    public Long delete(@PathVariable Long sid, @PathVariable Long aid, @CurrentUser Account account) {
        return applicantService.deleteApplicant(aid, account);
    }
}
