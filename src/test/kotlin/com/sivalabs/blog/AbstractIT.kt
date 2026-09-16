package com.sivalabs.blog

import com.sivalabs.blog.users.TokenHelper
import java.util.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.assertj.MockMvcTester
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration::class)
@ActiveProfiles("it")
abstract class AbstractIT {
    protected val adminAuthToken = createBasicAuthHeader("admin@gmail.com", "password")
    protected val userAuthToken = createBasicAuthHeader("siva@gmail.com", "password")

    @Autowired
    protected lateinit var tokenHelper: TokenHelper

    @Autowired
    protected lateinit var mockMvcTester: MockMvcTester

    @Autowired
    protected lateinit var restTestClient: RestTestClient

    fun createBasicAuthHeader(email: String, password: String): String =
        "Basic " + Base64.getEncoder().encodeToString("$email:$password".toByteArray())

    fun createBearerTokenHeader(email: String): String = "Bearer " + tokenHelper.generateToken(email)
}
