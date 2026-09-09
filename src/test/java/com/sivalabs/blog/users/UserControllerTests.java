package com.sivalabs.blog.users;

import com.sivalabs.blog.AbstractIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/test-data.sql")
class UserControllerTests extends AbstractIT {

    @Test
    void shouldCreateUserSuccessfully() {
        UserDto createdUser = restTestClient
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
                .isCreated()
                .returnResult(UserDto.class)
                .getResponseBody();

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.id()).isNotNull();
        assertThat(createdUser.name()).isEqualTo("User123");
        assertThat(createdUser.email()).isEqualTo("user123@gmail.com");
        assertThat(createdUser.password()).isNull();
        assertThat(createdUser.role()).isEqualTo(Role.ROLE_USER);
    }

    @Test
    void shouldGetAllUsersAsAdmin() {
        String header = createBearerTokenHeader("admin@gmail.com");
        restTestClient
                .get()
                .uri("/api/users")
                .header(HttpHeaders.AUTHORIZATION, header)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].email").isEqualTo("admin@gmail.com")
                .jsonPath("$[0].password").doesNotExist()
                .jsonPath("$[1].email").isEqualTo("siva@gmail.com")
                .jsonPath("$[1].password").doesNotExist();
    }

    @Test
    void shouldRejectGetAllUsersWithoutAuthentication() {
        restTestClient
                .get()
                .uri("/api/users")
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    void shouldRejectGetAllUsersForNonAdminUser() {
        String header = createBearerTokenHeader("siva@gmail.com");
        restTestClient
                .get()
                .uri("/api/users")
                .header(HttpHeaders.AUTHORIZATION, header)
                .exchange()
                .expectStatus()
                .isForbidden();
    }
}
