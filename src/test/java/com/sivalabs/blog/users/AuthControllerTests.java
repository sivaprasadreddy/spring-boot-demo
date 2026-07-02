package com.sivalabs.blog.users;

import com.sivalabs.blog.AbstractIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

class AuthControllerTests extends AbstractIT {

    @Test
    void shouldLoginSuccessfully() {
        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                          {
                            "email":"siva@gmail.com",
                            "password":"siva"
                        }
                        """)
                .exchange();

        assertThat(result)
                .hasStatus(OK)
                .bodyJson()
                .convertTo(AuthController.LoginResponse.class)
                .satisfies(res -> {
                    assertThat(res.token()).isNotEmpty();
                    assertThat(res.email()).isEqualTo("siva@gmail.com");
                    assertThat(res.name()).isEqualTo("Siva Prasad");
                });
    }
}
