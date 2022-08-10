package com.astar.gostudy_be.domain.study.controller;

import com.astar.gostudy_be.annotation.CurrentUser;
import com.astar.gostudy_be.domain.study.dto.ParticipantListDto;
import com.astar.gostudy_be.domain.study.service.ParticipantService;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping("/api/v1/studies/{id}/participants")
    public List<ParticipantListDto> participants(@PathVariable Long id) {
        return participantService.findAllParticipantsByStudyId(id);
    }

    @DeleteMapping("/api/v1/studies/{sid}/participants/{pid}")
    public Long delete(@PathVariable Long sid, @PathVariable Long pid, @CurrentUser Account account) {
        return participantService.deleteParticipant(pid, account);
    }
}
