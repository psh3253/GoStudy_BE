package com.astar.gostudy_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;

@EnableJpaAuditing
@SpringBootApplication
public class GoStudyBeApplication {

    public static void main(String[] args) {
        createUploadsDirectory();
        SpringApplication.run(GoStudyBeApplication.class, args);
    }

    public static void createUploadsDirectory() {
        File uploadDirectory = new File("C://uploads");
        if (!uploadDirectory.exists())
            uploadDirectory.mkdir();
        File imageDirectory = new File("C://uploads/images");
        if (!imageDirectory.exists())
            imageDirectory.mkdir();
        File thumbnailImageDirectory = new File("C://uploads/thumbnail_images");
        if(!thumbnailImageDirectory.exists())
            thumbnailImageDirectory.mkdir();
    }
}
