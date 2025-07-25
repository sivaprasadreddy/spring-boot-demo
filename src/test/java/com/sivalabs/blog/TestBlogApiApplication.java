package com.sivalabs.blog;

import org.springframework.boot.SpringApplication;

public class TestBlogApiApplication {

    public static void main(String[] args) {
        System.setProperty("spring.docker.compose.enabled", "false");
        SpringApplication.from(BlogApiApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
