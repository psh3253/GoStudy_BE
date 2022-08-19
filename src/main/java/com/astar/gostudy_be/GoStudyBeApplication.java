package com.astar.gostudy_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;

@SpringBootApplication
public class GoStudyBeApplication {

    public static void main(String[] args) {
        createUploadsDirectory();
        SpringApplication.run(GoStudyBeApplication.class, args);
    }

    public static void createUploadsDirectory() {
        File studyImageDirectory = new File("C://uploads/study/images");
        studyImageDirectory.mkdirs();
        File studyThumbnailImageDirectory = new File("C://uploads/study/thumbnail_images");
        studyThumbnailImageDirectory.mkdirs();
        File profileImageDirectory = new File("C://uploads/profile/images");
        profileImageDirectory.mkdirs();
        File profileThumbnailImageDirectory = new File("C://uploads/profile/thumbnail_images");
        profileThumbnailImageDirectory.mkdirs();
        File postImageDirectory = new File("C://uploads/post/images");
        postImageDirectory.mkdirs();
        File postThumbnailImageDirectory = new File("C://uploads/post/thumbnail_images");
        postThumbnailImageDirectory.mkdirs();
    }
}
