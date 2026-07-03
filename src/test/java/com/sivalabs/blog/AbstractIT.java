package com.sivalabs.blog;

import com.sivalabs.blog.users.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.Base64;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("it")
public abstract class AbstractIT {
    protected String ADMIN_AUTH_TOKEN = this.createBasicAuthHeader("admin@gmail.com", "password");
    protected String USER_AUTH_TOKEN = this.createBasicAuthHeader("siva@gmail.com", "password");

    @Autowired
    protected TokenHelper tokenHelper;

    @Autowired
    protected MockMvcTester mockMvcTester;

    @Autowired
    protected RestTestClient restTestClient;

    public String createBasicAuthHeader(String email, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((email+ ":"+password).getBytes());
    }

    public String createBearerTokenHeader(String email) {
        return "Bearer " + tokenHelper.generateToken(email);
    }
}
