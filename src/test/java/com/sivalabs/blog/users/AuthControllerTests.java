package com.sivalabs.blog.users;

import com.sivalabs.blog.AbstractIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

class AuthControllerTests extends AbstractIT {

    @Test
    void shouldLoginSuccessfully() {
        AuthController.LoginResponse loginResponse = restTestClient
                .post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "email":"siva@gmail.com",
                          "password":"password"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(AuthController.LoginResponse.class)
                .getResponseBody();

        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.token()).isNotBlank();
    }

    @Test
    void shouldFailToLoginWithInvalidCredentials() {
        restTestClient
                .post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "email":"siva@gmail.com",
                          "password":"wrong-pwd"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }
}