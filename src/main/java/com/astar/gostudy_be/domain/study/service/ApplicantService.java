package com.astar.gostudy_be.domain.study.service;

import com.astar.gostudy_be.domain.study.dto.ApplicantListDto;
import com.astar.gostudy_be.domain.study.entity.Applicant;
import com.astar.gostudy_be.domain.study.entity.Participant;
import com.astar.gostudy_be.domain.study.entity.Study;
import com.astar.gostudy_be.domain.study.repository.ApplicantRepository;
import com.astar.gostudy_be.domain.study.repository.ParticipantRepository;
import com.astar.gostudy_be.domain.study.repository.StudyRepository;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicantService {
    private final ApplicantRepository applicantRepository;

    private final ParticipantRepository participantRepository;

    private final StudyRepository studyRepository;

    @Transactional(readOnly = true)
    public List<ApplicantListDto> findAllApplicantsByStudyId(Long studyId) {
        return applicantRepository.findAllByStudyId(studyId).stream()
                .map(ApplicantListDto::new).collect(Collectors.toList());
    }

    @Transactional
    public Long acceptParticipation(Long applicantId, Account account) {
        Applicant applicant = applicantRepository.findById(applicantId).orElseThrow(() -> new IllegalArgumentException("신청자가 존재하지 않습니다."));
        if (!Objects.equals(applicant.getStudy().getAccount().getEmail(), account.getEmail())) {
            throw new RuntimeException("사용자가 스터디의 소유자랑 일치하지 않습니다.");
        }
        if(applicant.getStudy().getCurrentNumber() >= applicant.getStudy().getRecruitmentNumber()) {
            throw new RuntimeException("모집 인원을 초과할 수 없습니다.");
        }
        applicantRepository.delete(applicant);

        Participant participant = Participant.builder()
                .study(applicant.getStudy())
                .account(applicant.getAccount())
                .build();
        participantRepository.save(participant);

        studyRepository.save(applicant.getStudy().update(Study.builder().currentNumber(applicant.getStudy().getCurrentNumber() + 1).build()));
        return applicant.getId();
    }

    @Transactional
    public Long deleteApplicant(Long applicantId, Account account) {
        Applicant applicant = applicantRepository.findById(applicantId).orElseThrow(() -> new IllegalArgumentException("신청자가 존재하지 않습니다."));
        if (!Objects.equals(applicant.getStudy().getAccount().getEmail(), account.getEmail())) {
            throw new RuntimeException("사용자가 스터디의 소유자랑 일치하지 않습니다.");
        }
        applicantRepository.delete(applicant);
        return applicant.getId();
    }
}
