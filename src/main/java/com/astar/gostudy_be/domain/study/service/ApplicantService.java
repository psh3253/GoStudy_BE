package com.astar.gostudy_be.domain.study.service;

import com.astar.gostudy_be.domain.study.dto.ApplicantListDto;
import com.astar.gostudy_be.domain.study.repository.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    @Transactional(readOnly = true)
    public List<ApplicantListDto> findAllApplicantsByStudyId(Long studyId) {
        return applicantRepository.findAllByStudyId(studyId).stream()
                .map(ApplicantListDto::new).collect(Collectors.toList());
    }
}
