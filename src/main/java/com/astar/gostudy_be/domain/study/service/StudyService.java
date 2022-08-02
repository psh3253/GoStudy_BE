package com.astar.gostudy_be.domain.study.service;

import com.astar.gostudy_be.domain.study.dto.*;
import com.astar.gostudy_be.domain.study.entity.Category;
import com.astar.gostudy_be.domain.study.entity.Participant;
import com.astar.gostudy_be.domain.study.entity.Study;
import com.astar.gostudy_be.domain.study.entity.StudyImage;
import com.astar.gostudy_be.domain.study.repository.CategoryRepository;
import com.astar.gostudy_be.domain.study.repository.ParticipantRepository;
import com.astar.gostudy_be.domain.study.repository.StudyImageRepository;
import com.astar.gostudy_be.domain.study.repository.StudyRepository;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StudyService {
    private final StudyRepository studyRepository;

    private final CategoryRepository categoryRepository;

    private final StudyImageRepository studyImageRepository;

    private final ParticipantRepository participantRepository;

    @Transactional(readOnly = true)
    public List<StudyListDto> findAllStudies() {
        return studyRepository.findAllByIsRecruitingIsTrue().stream()
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
    public StudyDto findStudy(Long studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        return new StudyDto(study);
    }

    @Transactional
    public Long createStudy(StudyCreateDto studyCreateDto, Account account) {
        if (studyCreateDto.getImage() != null) {
            String extension = StringUtils.getFilenameExtension(studyCreateDto.getImage().getOriginalFilename());
            String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + '.' + extension;

            try {
                File uploadDirectory = new File("C://uploads");
                if (!uploadDirectory.exists())
                    uploadDirectory.mkdir();
                File imageDirectory = new File("C://uploads/image");
                if (!imageDirectory.exists())
                    imageDirectory.mkdir();
                File imageFile = new File("C://uploads/image/" + filename);
                studyCreateDto.getImage().transferTo(imageFile);

                File thumbnailImageDirectory = new File("C://uploads/thumbnail_image");
                if(!thumbnailImageDirectory.exists())
                    thumbnailImageDirectory.mkdir();
                File thumbnailImageFile = new File("C://uploads/thumbnail_image/" + "thumbnail_" + filename);
                Image image = ImageIO.read(imageFile);
                Image resizedImage = image.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
                BufferedImage newImage = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
                Graphics graphics= newImage.getGraphics();
                graphics.drawImage(resizedImage, 0, 0,null);
                graphics.dispose();
                ImageIO.write(newImage, extension, thumbnailImageFile);

                StudyImage studyImage = StudyImage.builder()
                        .originalFileName(studyCreateDto.getImage().getOriginalFilename())
                        .filename(filename)
                        .build();
                StudyImage savedStudyImage = studyImageRepository.save(studyImage);

                Category category = categoryRepository.findById(studyCreateDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));
                Study savedStudy = studyRepository.save(studyCreateDto.toEntity(category, account, savedStudyImage));

                Participant creator = Participant.builder()
                        .study(savedStudy)
                        .account(account)
                        .build();
                participantRepository.save(creator);
                return savedStudy.getId();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return -1L;
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

    @Transactional
    public Long closeStudy(Long studyId, Account account) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        if(!Objects.equals(study.getAccount().getEmail(), account.getEmail())) {
            throw new IllegalArgumentException("사용자가 스터디의 소유자랑 일치하지 않습니다.");
        }
        return studyRepository.save(study.update(Study.builder().isRecruiting(false).build())).getId();
    }
}
