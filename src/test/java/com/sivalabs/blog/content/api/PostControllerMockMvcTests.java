package com.sivalabs.blog.content.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.sivalabs.blog.AbstractIT;
import com.sivalabs.blog.content.core.models.CommentDto;
import com.sivalabs.blog.content.core.models.PostDto;
import com.sivalabs.blog.shared.models.PagedResult;
import com.sivalabs.blog.shared.models.Role;
import com.sivalabs.blog.users.core.models.UserDto;
import java.net.URI;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@Sql("/test-data.sql")
class PostControllerMockMvcTests extends AbstractIT {

    @Test
    void shouldGetPosts() {
        MvcTestResult result = mockMvcTester.get().uri("/api/posts").exchange();
        assertThat(result).hasStatus(OK).bodyJson().convertTo(PagedResult.class).satisfies(p -> {
            PagedResult<PostDto> pagedResult = (PagedResult<PostDto>) p;
            assertThat(pagedResult.data()).hasSize(5);
            assertThat(pagedResult.currentPageNo()).isEqualTo(1);
            assertThat(pagedResult.totalPages()).isEqualTo(2);
            assertThat(pagedResult.totalElements()).isEqualTo(9);
            assertThat(pagedResult.hasNextPage()).isTrue();
            assertThat(pagedResult.hasPreviousPage()).isFalse();
        });
    }

    @Test
    void shouldSearchPosts() {
        MvcTestResult result =
                mockMvcTester.get().uri("/api/posts?query=spring").exchange();
        assertThat(result).hasStatus(OK).bodyJson().convertTo(PagedResult.class).satisfies(p -> {
            PagedResult<PostDto> pagedResult = (PagedResult<PostDto>) p;
            assertThat(pagedResult.data()).hasSize(4);
        });
    }

    @Test
    void shouldGetPostBySlug() {
        MvcTestResult result = mockMvcTester
                .get()
                .uri("/api/posts/{slug}", "introducing-springboot")
                .exchange();
        assertThat(result).hasStatus(OK).bodyJson().convertTo(PostDto.class).satisfies(postDto -> {
            assertThat(postDto).isNotNull();
            assertThat(postDto.id()).isEqualTo(2);
            assertThat(postDto.title()).isEqualTo("SpringBoot: Introducing SpringBoot");
            assertThat(postDto.slug()).isEqualTo("introducing-springboot");
        });
    }

    @Test
    void shouldGetPostComments() {
        MvcTestResult result = mockMvcTester
                .get()
                .uri("/api/posts/{slug}/comments", "introducing-springboot")
                .exchange();
        assertThat(result)
                .hasStatus(OK)
                .bodyJson()
                .convertTo(InstanceOfAssertFactories.list(CommentDto.class))
                .satisfies(commentDtos -> {
                    assertThat(commentDtos).hasSize(2);
                });
    }

    @Test
    void shouldCreateCommentSuccessfully() {
        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/posts/{slug}/comments", "introducing-springboot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                            "name": "Siva",
                            "email": "siva@gmail.com",
                            "content": "Test comment"
                        }
                        """)
                .exchange();
        assertThat(result).hasStatus(CREATED);
    }

    @Test
    void shouldCreatePostSuccessfully() {
        UserDto userDto = new UserDto(2L, "Siva", "siva@gmail.com", "", Role.ROLE_USER);
        String token = this.createToken(userDto);

        MvcTestResult result = mockMvcTester
                .post()
                .uri("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .content(
                        """
                        {
                          "title":"My New Post",
                          "slug":"my-new-post",
                          "content":"This is my test pose"
                        }
                        """)
                .exchange();
        assertThat(result).hasStatus(CREATED).headers().satisfies(headers -> {
            URI location = headers.getLocation();
            assertThat(location).isNotNull();
            assertThat(location.toString()).endsWith("/api/posts/my-new-post");
        });
    }

    @Test
    void shouldUpdatePostSuccessfully() {
        UserDto userDto = new UserDto(2L, "Siva", "siva@gmail.com", "", Role.ROLE_USER);
        String token = this.createToken(userDto);

        MvcTestResult result = mockMvcTester
                .put()
                .uri("/api/posts/{slug}", "installing-linuxmint")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .content(
                        """
                        {
              "title":"Installing LinuxMint OS",
              "slug":"installing-linuxmint-os",
              "content":"Installing LinuxMint 22"
            }
            """)
                .exchange();
        assertThat(result).hasStatus(OK).headers().satisfies(headers -> {
            URI location = headers.getLocation();
            assertThat(location).isNotNull();
            assertThat(location.toString()).endsWith("/api/posts/installing-linuxmint-os");
        });
    }
}
