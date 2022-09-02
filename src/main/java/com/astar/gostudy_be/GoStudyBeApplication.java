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
import java.io.InputStream;
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
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win"))
            Config.UPLOAD_FILE_PATH = "C://";
        else
            Config.UPLOAD_FILE_PATH = "/home/";
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
            InputStream profileInputStream = GoStudyBeApplication.class.getResourceAsStream( "/static/profile/thumbnail_default.png");
//            Resource profileResource = context.getResource("classpath:" + File.separator + "static" + File.separator + "profile" + File.separator +"thumbnail_default.png");
//            File srcProfileImage = new File(profileResource.getURI());
            File dstProfileImage = new File(Config.UPLOAD_FILE_PATH + "uploads" + File.separator + "profile" + File.separator + "thumbnail_images" + File.separator + "thumbnail_default.png");

            Files.copy(profileInputStream, dstProfileImage.toPath(), StandardCopyOption.REPLACE_EXISTING);

            InputStream studyInputStream = GoStudyBeApplication.class.getResourceAsStream( "/static/study/thumbnail_default.jpg");
//            Resource studyResource = context.getResource("classpath:" + File.separator + "static" + File.separator + "study" + File.separator +"thumbnail_default.jpg");
//            File srcStudyImage = new File(studyResource.getURI());
            File dstStudyImage = new File(Config.UPLOAD_FILE_PATH + "uploads" + File.separator + "study" + File.separator + "thumbnail_images" + File.separator + "thumbnail_default.jpg");

            Files.copy(studyInputStream, dstStudyImage.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
