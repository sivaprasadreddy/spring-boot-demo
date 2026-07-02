package com.sivalabs.blog;

import com.sivalabs.blog.users.JwtTokenHelper;
import com.sivalabs.blog.users.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("it")
public abstract class AbstractIT {

    @Autowired
    protected MockMvcTester mockMvcTester;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    public String createToken(UserDto userDto) {
        return jwtTokenHelper.generateToken(userDto).token();
    }
}
