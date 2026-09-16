package com.sivalabs.blog

import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    System.setProperty("spring.docker.compose.enabled", "false")
    SpringApplication.from(BlogApplication::main)
        .with(TestcontainersConfiguration::class.java)
        .run(*args)
}
