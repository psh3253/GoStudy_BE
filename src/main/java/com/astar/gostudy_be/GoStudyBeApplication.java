package com.astar.gostudy_be;

import com.astar.gostudy_be.config.Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class GoStudyBeApplication {

    private final ApplicationContext nonStaticContext;

    static ApplicationContext context;

    @PostConstruct
    private void initStatic() {
        context = this.nonStaticContext;
    }

    public static void main(String[] args) {
        createUploadsDirectory();
        SpringApplication.run(GoStudyBeApplication.class, args);
        copyDefaultImage();
    }

    public static void createUploadsDirectory() {
        File studyImageDirectory = new File(Config.UPLOAD_FILE_PATH + "uploads/study/images");
        studyImageDirectory.mkdirs();
        File studyThumbnailImageDirectory = new File(Config.UPLOAD_FILE_PATH + "uploads/study/thumbnail_images");
        studyThumbnailImageDirectory.mkdirs();
        File profileImageDirectory = new File(Config.UPLOAD_FILE_PATH + "uploads/profile/images");
        profileImageDirectory.mkdirs();
        File profileThumbnailImageDirectory = new File(Config.UPLOAD_FILE_PATH + "uploads/profile/thumbnail_images");
        profileThumbnailImageDirectory.mkdirs();
        File postImageDirectory = new File(Config.UPLOAD_FILE_PATH + "uploads/post/images");
        postImageDirectory.mkdirs();
        File postThumbnailImageDirectory = new File(Config.UPLOAD_FILE_PATH + "uploads/post/thumbnail_images");
        postThumbnailImageDirectory.mkdirs();

    }

    public static void copyDefaultImage() {
        try {
            Resource profileResource = context.getResource("classpath:/static/profile/thumbnail_default.png");
            File srcProfileImage = new File(profileResource.getURI());
            File dstProfileImage = new File(Config.UPLOAD_FILE_PATH + "uploads/profile/thumbnail_images/thumbnail_default.png");

            Files.copy(srcProfileImage.toPath(), dstProfileImage.toPath(), StandardCopyOption.REPLACE_EXISTING);

            Resource studyResource = context.getResource("classpath:/static/study/thumbnail_default.jpg");
            File srcStudyImage = new File(studyResource.getURI());
            File dstStudyImage = new File(Config.UPLOAD_FILE_PATH + "uploads/study/thumbnail_images/thumbnail_default.jpg");

            Files.copy(srcStudyImage.toPath(), dstStudyImage.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
