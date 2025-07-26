package com.sivalabs.blog.users.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.blog.AbstractIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

class AuthControllerTests extends AbstractIT {

    @Test
    @DisplayName("MockMvc: Given valid credentials, user should be able to login successfully")
    void shouldLoginSuccessfullyUsingMockMvc() throws Exception {
        mvc.perform(
                        post("/api/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                  {
                      "email":"siva@gmail.com",
                      "password":"siva"
                  }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token", notNullValue()))
                .andExpect(jsonPath("email", equalTo("siva@gmail.com")))
                .andExpect(jsonPath("name", equalTo("Siva Prasad")));
    }

    @Test
    @DisplayName("MockMvcTester: Given valid credentials, user should be able to login successfully")
    void shouldLoginSuccessfullyUsingMockMvcTester() {
        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
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

    @Test
    @DisplayName("RestAssured: Given valid credentials, user should be able to login successfully")
    void shouldLoginSuccessfullyUsingRestAssured() {
        given().contentType("application/json")
                .body(
                        """
                    {
                      "email":"siva@gmail.com",
                      "password":"siva"
                  }
                  """)
                .when()
                .post("/api/login")
                .then()
                .statusCode(OK.value())
                .assertThat()
                .body("token", not(blankOrNullString()))
                .body("email", equalTo("siva@gmail.com"))
                .body("name", equalTo("Siva Prasad"));
    }
}
