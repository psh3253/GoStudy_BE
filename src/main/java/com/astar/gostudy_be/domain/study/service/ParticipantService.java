package com.astar.gostudy_be.domain.study.service;

import com.astar.gostudy_be.domain.study.dto.ParticipantListDto;
import com.astar.gostudy_be.domain.study.entity.Participant;
import com.astar.gostudy_be.domain.study.entity.Study;
import com.astar.gostudy_be.domain.study.repository.ParticipantRepository;
import com.astar.gostudy_be.domain.study.repository.StudyRepository;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    private final StudyRepository studyRepository;

    @Transactional(readOnly = true)
    public List<ParticipantListDto> findAllParticipantsByStudyId(Long studyId) {
        return participantRepository.findAllByStudyId(studyId).stream().map(
                ParticipantListDto::new
        ).collect(Collectors.toList());
    }

    @Transactional
    public Long deleteParticipant(Long participantId, Account account) {
        Participant participant = participantRepository.findById(participantId).orElseThrow(() -> new IllegalArgumentException("참석자가 존재하지 않습니다."));
        if(!Objects.equals(participant.getStudy().getAccount().getEmail(), account.getEmail())) {
            throw new IllegalArgumentException("사용자가 스터디의 소유자랑 일치하지 않습니다.");
        }
        participantRepository.delete(participant);
        Study study = participant.getStudy();
        studyRepository.save(study.update(Study.builder().currentNumber(study.getCurrentNumber() - 1).build()));
        return participant.getId();
    }
}
