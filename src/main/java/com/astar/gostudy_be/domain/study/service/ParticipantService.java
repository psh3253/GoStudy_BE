package com.astar.gostudy_be.domain.study.service;

import com.astar.gostudy_be.domain.study.dto.ParticipantListDto;
import com.astar.gostudy_be.domain.study.repository.ParticipantRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    @Transactional(readOnly = true)
    public List<ParticipantListDto> findAllParticipantsByStudyId(Long studyId) {
        return participantRepository.findAllByStudyId(studyId).stream().map(
                ParticipantListDto::new
        ).collect(Collectors.toList());
    }
}
