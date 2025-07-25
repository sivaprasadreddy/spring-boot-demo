package com.sivalabs.blog;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.sivalabs.blog.users.core.JwtTokenHelper;
import com.sivalabs.blog.users.core.models.UserDto;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("it")
public abstract class AbstractIT {

    // Note: Using RestAssured, TestRestTemplate, etc. to demonstrate how to test using different approaches.
    // In real projects, you should better stick with one approach.

    @LocalServerPort
    public int serverPort;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected MockMvcTester mvc;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @PostConstruct
    public void init() {
        RestAssured.port = serverPort;
        RestAssured.urlEncodingEnabled = false;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public String createToken(UserDto userDto) {
        return jwtTokenHelper.generateToken(userDto).token();
    }
}
