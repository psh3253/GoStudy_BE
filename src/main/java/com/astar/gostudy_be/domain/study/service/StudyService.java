package com.astar.gostudy_be.domain.study.service;

import com.astar.gostudy_be.domain.study.dto.CategoryListDto;
import com.astar.gostudy_be.domain.study.dto.StudyCreateDto;
import com.astar.gostudy_be.domain.study.dto.StudyListDto;
import com.astar.gostudy_be.domain.study.dto.StudyUpdateDto;
import com.astar.gostudy_be.domain.study.entity.Category;
import com.astar.gostudy_be.domain.study.entity.Study;
import com.astar.gostudy_be.domain.study.repository.CategoryRepository;
import com.astar.gostudy_be.domain.study.repository.StudyRepository;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudyService {
    private final StudyRepository studyRepository;

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<StudyListDto> findAllStudies() {
        return studyRepository.findAll().stream()
                .map(StudyListDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudyListDto> findAllStudiesByCategoryId(Long categoryId) {
        return studyRepository.findAllByCategoryId(categoryId).stream()
                .map(StudyListDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryListDto> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryListDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createStudy(StudyCreateDto studyCreateDto, Account account) {
        Category category = categoryRepository.findById(studyCreateDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));
        return studyRepository.save(studyCreateDto.toEntity(category, account)).getId();
    }

    @Transactional
    public Long updateStudy(StudyUpdateDto studyUpdateDto, Long studyId, Account account) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        Category category = null;
        if (studyUpdateDto.getCategoryId() != null) {
            category = categoryRepository.findById(studyUpdateDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));
        }
        if (!Objects.equals(studyUpdateDto.getId(), studyId)) {
            throw new IllegalArgumentException("스터디 번호가 일치하지 않습니다.");
        } else if (!Objects.equals(study.getAccount().getEmail(), account.getEmail())) {
            throw new IllegalArgumentException("사용자가 스터디의 소유자랑 일치하지 않습니다.");
        }
        return studyRepository.save(study.update(studyUpdateDto.toEntity(category))).getId();
    }

    @Transactional
    public void deleteStudy(Long studyId, Account account) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        if (!Objects.equals(study.getAccount().getEmail(), account.getEmail())) {
            throw new IllegalArgumentException("사용자가 스터디의 소유자랑 일치하지 않습니다.");
        }
        studyRepository.delete(study);
    }
}
