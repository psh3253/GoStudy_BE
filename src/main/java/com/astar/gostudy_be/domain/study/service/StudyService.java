package com.astar.gostudy_be.domain.study.service;

import com.astar.gostudy_be.domain.study.dto.*;
import com.astar.gostudy_be.domain.study.entity.*;
import com.astar.gostudy_be.domain.study.repository.*;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StudyService {
    private final StudyRepository studyRepository;

    private final CategoryRepository categoryRepository;

    private final StudyImageRepository studyImageRepository;

    private final ParticipantRepository participantRepository;

    private final ApplicantRepository applicantRepository;

    @Transactional(readOnly = true)
    public List<StudyListDto> findAllStudies() {
        return studyRepository.findAllByIsRecruitingIsTrueAndVisibilityEquals(Visibility.PUBLIC).stream()
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

    @Transactional(readOnly = true)
    public List<StudyListDto> findAllStudiesByAccount(Account account) {
        return studyRepository.findAllByAccountEmail(account.getEmail()).stream()
                .map(StudyListDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StudyDto findStudyById(Long studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        return new StudyDto(study);
    }

    @Transactional(readOnly = true)
    public Long findStudyIdByAccessUrl(String accessUrl) {
        Study study = studyRepository.findStudyByAccessUrl(accessUrl).orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        return study.getId();
    }

    @Transactional
    public Long createStudy(StudyCreateDto studyCreateDto, Account account) {
        String filename;
        if (studyCreateDto.getImage() != null) {
            String extension = StringUtils.getFilenameExtension(studyCreateDto.getImage().getOriginalFilename());
            filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + '.' + extension;

            try {
                File imageFile = new File("C://uploads/study/images/" + filename);
                studyCreateDto.getImage().transferTo(imageFile);

                File thumbnailImageFile = new File("C://uploads/study/thumbnail_images/" + "thumbnail_" + filename);
                Image image = ImageIO.read(imageFile);
                Image resizedImage = image.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                BufferedImage newImage = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
                Graphics graphics = newImage.getGraphics();
                graphics.drawImage(resizedImage, 0, 0, null);
                graphics.dispose();
                ImageIO.write(newImage, extension, thumbnailImageFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            filename = "default.jpg";
        }
        StudyImage studyImage = StudyImage.builder()
                .filename(filename)
                .build();
        StudyImage savedStudyImage = studyImageRepository.save(studyImage);

        Category category = categoryRepository.findById(studyCreateDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

        String randomString = "";
        do {
            Random random = new Random();
            randomString = random.ints(65, 123)
                    .filter(i -> (i >= 65 && i <= 90) || (i >= 97 && i <= 122))
                    .limit(10)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        } while (studyRepository.countByAccessUrl(randomString) != 0);
        Study savedStudy = studyRepository.save(studyCreateDto.toEntity(category, account, savedStudyImage, randomString));

        Participant creator = Participant.builder()
                .study(savedStudy)
                .account(account)
                .build();
        participantRepository.save(creator);
        return savedStudy.getId();
    }

    @Transactional
    public Long updateStudy(StudyUpdateDto studyUpdateDto, Long studyId, Account account) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        Category category = categoryRepository.findById(studyUpdateDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));
        if (!Objects.equals(studyUpdateDto.getId(), studyId)) {
            throw new RuntimeException("스터디 번호가 일치하지 않습니다.");
        } else if (!Objects.equals(study.getAccount().getEmail(), account.getEmail())) {
            throw new RuntimeException("사용자가 스터디의 소유자랑 일치하지 않습니다.");
        }
        return studyRepository.save(study.update(studyUpdateDto.toEntity(category))).getId();
    }

    @Transactional
    public void deleteStudy(Long studyId, Account account) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        if (!Objects.equals(study.getAccount().getEmail(), account.getEmail())) {
            throw new RuntimeException("사용자가 스터디의 소유자랑 일치하지 않습니다.");
        }
        studyRepository.delete(study);
    }

    @Transactional
    public Long closeStudy(Long studyId, Account account) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        if (!Objects.equals(study.getAccount().getEmail(), account.getEmail())) {
            throw new RuntimeException("사용자가 스터디의 소유자랑 일치하지 않습니다.");
        }
        return studyRepository.save(study.update(Study.builder().isRecruiting(false).build())).getId();
    }

    @Transactional
    public Long participateStudy(Long studyId, String message, Account account) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        if(study.getCurrentNumber() >= study.getRecruitmentNumber())
            throw new RuntimeException("모집 인원을 초과할 수 없습니다.");
        if (study.getJoinType().name().equals("FREE")) {
            Participant participant = Participant.builder()
                    .study(study)
                    .account(account)
                    .build();
            studyRepository.save(study.update(Study.builder()
                    .currentNumber(study.getCurrentNumber() + 1)
                    .build()));
            log.info(study.getCurrentNumber().toString());
            return participantRepository.save(participant).getId();
        } else {
            Applicant applicantParticipant = Applicant.builder()
                    .message(message)
                    .study(study)
                    .account(account)
                    .build();
            return applicantRepository.save(applicantParticipant).getId();
        }
    }

    @Transactional
    public Long withdrawStudy(Long studyId, Account account) {
        Participant participant = participantRepository.findByStudyIdAndAccountEmail(studyId, account.getEmail()).orElseThrow(() -> new IllegalArgumentException("참석자가 존재하지 않습니다."));
        participantRepository.delete(participant);

        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        studyRepository.save(study.update(Study.builder()
                .currentNumber(study.getCurrentNumber() - 1)
                .build()));
        return participant.getId();
    }

    @Transactional
    public Long cancelApplicationStudy(Long studyId, Account account) {
        Applicant applicant = applicantRepository.findByStudyIdAndAccountEmail(studyId, account.getEmail()).orElseThrow(() -> new IllegalArgumentException("신청자가 존재하지 않습니다."));
        applicantRepository.delete(applicant);
        return applicant.getId();
    }
}
