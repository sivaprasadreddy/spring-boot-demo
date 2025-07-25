package com.sivalabs.blog.users.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.CREATED;

import com.sivalabs.blog.AbstractIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTests extends AbstractIT {

    @Test
    @DisplayName("Given valid user details, user should be created successfully")
    void shouldCreateUserSuccessfully() {
        given().contentType("application/json")
                .body(
                        """
              {
                  "name":"User123",
                  "email":"user123@gmail.com",
                  "password":"secret"
                }
              """)
                .when()
                .post("/api/users")
                .then()
                .statusCode(CREATED.value())
                .assertThat()
                .body("name", equalTo("User123"))
                .body("email", equalTo("user123@gmail.com"))
                .body("role", equalTo("ROLE_USER"));
    }
}
