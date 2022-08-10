package com.astar.gostudy_be.domain.post.service;

import com.astar.gostudy_be.domain.post.dto.PostCreateDto;
import com.astar.gostudy_be.domain.post.dto.PostDto;
import com.astar.gostudy_be.domain.post.dto.PostListDto;
import com.astar.gostudy_be.domain.post.entity.Post;
import com.astar.gostudy_be.domain.post.repository.PostRepository;
import com.astar.gostudy_be.domain.study.entity.Study;
import com.astar.gostudy_be.domain.study.repository.StudyRepository;
import com.astar.gostudy_be.domain.user.entity.Account;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final StudyRepository studyRepository;

    @Transactional(readOnly = true)
    public List<PostListDto> findAllPostsByStudyId(Long studyId) {
        return postRepository.findAllByStudyId(studyId).stream()
                .map(PostListDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostDto findPostByPostId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return new PostDto(post);
    }

    @Transactional
    public Long createPost(PostCreateDto postCreateDto, Long studyId, Account account)  {
        String filename = null;
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        if (postCreateDto.getImage() != null) {
            String extension = StringUtils.getFilenameExtension(postCreateDto.getImage().getOriginalFilename());
            filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + '.' + extension;

            try {
                File imageFile = new File("C://uploads/post/images/" + filename);
                postCreateDto.getImage().transferTo(imageFile);

                File thumbnailImageFile = new File("C://uploads/post/thumbnail_images/" + "thumbnail_" + filename);
                Image image = ImageIO.read(imageFile);
                Image resizedImage = image.getScaledInstance(640, 640, Image.SCALE_SMOOTH);
                BufferedImage newImage = new BufferedImage(640, 640, BufferedImage.TYPE_INT_RGB);
                Graphics graphics = newImage.getGraphics();
                graphics.drawImage(resizedImage, 0, 0, null);
                graphics.dispose();
                ImageIO.write(newImage, extension, thumbnailImageFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Post post = postCreateDto.toEntity(study, account, filename);
        return postRepository.save(post).getId();
    }

    @Transactional
    public Long deletePost(Long postId, Account account) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if(!Objects.equals(post.getAccount().getEmail(), account.getEmail())) {
            throw new RuntimeException("사용자가 게시글의 소유자랑 일치하지 않습니다.");
        }
        postRepository.delete(post);
        return post.getId();
    }
}
