package com.sivalabs.blog.notification;

import com.sivalabs.blog.ApplicationProperties;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final ApplicationProperties properties;

    public EmailService(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Async
    public void send(String subject, String content) {
        String supportEmail = properties.supportEmail();
        this.send(subject, List.of(supportEmail), content);
    }

    @Async
    public void send(String subject, List<String> to, String content) {
        String supportEmail = properties.supportEmail();
        String email = """
                ===============================================
                From: %s
                To: %s
                Subject: %s

                %s
                ===============================================
                """.formatted(supportEmail, String.join(",", to), subject, content);
        log.info(email);
    }
}
