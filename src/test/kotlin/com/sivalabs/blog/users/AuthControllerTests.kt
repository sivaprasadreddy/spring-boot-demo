package com.sivalabs.blog.users

import com.sivalabs.blog.AbstractIT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType

class AuthControllerTests : AbstractIT() {
    @Test
    fun shouldLoginSuccessfully() {
        val loginResponse = requireNotNull(restTestClient.post()
            .uri("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "email":"siva@gmail.com",
                  "password":"password"
                }
                """.trimIndent(),
            )
            .exchange()
            .expectStatus().isOk()
            .returnResult(AuthController.LoginResponse::class.java)
            .responseBody)

        assertThat(loginResponse.token()).isNotBlank()
    }

    @Test
    fun shouldFailToLoginWithInvalidCredentials() {
        restTestClient.post()
            .uri("/api/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                """
                {
                  "email":"siva@gmail.com",
                  "password":"wrong-pwd"
                }
                """.trimIndent(),
            )
            .exchange()
            .expectStatus().isUnauthorized()
    }
}
