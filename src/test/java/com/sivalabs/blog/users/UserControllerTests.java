package com.sivalabs.blog.users;

import com.sivalabs.blog.AbstractIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class UserControllerTests extends AbstractIT {

    @Test
    void shouldCreateUserSuccessfully() {
        restTestClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "name":"User123",
                          "email":"user123@gmail.com",
                          "password":"Secret@121212"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void shouldGetAllUsersAsAdmin() {
        restTestClient
                .get()
                .uri("/api/users")
                .header(HttpHeaders.AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
