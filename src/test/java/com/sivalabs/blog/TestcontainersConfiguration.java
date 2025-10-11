package com.sivalabs.blog;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@Testcontainers
public class TestcontainersConfiguration {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18-alpine");

    @Bean
    @Qualifier("mailhog") GenericContainer<?> mailhog() {
        return new GenericContainer<>("mailhog/mailhog:v1.0.1").withExposedPorts(1025);
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgres() {
        return postgres;
    }

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDBContainer() {
        return new MongoDBContainer(DockerImageName.parse("mongo:8.0.0"));
    }

    @Bean
    DynamicPropertyRegistrar dynamicPropertyRegistrar(@Qualifier("mailhog") GenericContainer<?> mailhog) {
        return (registry) -> {
            registry.add("spring.mail.host", mailhog::getHost);
            registry.add("spring.mail.port", mailhog::getFirstMappedPort);
        };
    }
}
