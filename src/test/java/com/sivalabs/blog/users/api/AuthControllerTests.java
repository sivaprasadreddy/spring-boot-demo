package com.sivalabs.blog.users.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.OK;

import com.sivalabs.blog.AbstractIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthControllerTests extends AbstractIT {

    @Test
    @DisplayName("Given valid credentials, user should be able to login successfully")
    void shouldLoginSuccessfully() {
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
