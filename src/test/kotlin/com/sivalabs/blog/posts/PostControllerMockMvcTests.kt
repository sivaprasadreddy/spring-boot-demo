package com.sivalabs.blog.posts

import com.sivalabs.blog.AbstractIT
import com.sivalabs.blog.shared.PagedResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql

@Sql("/test-data.sql")
@TestPropertySource(properties = ["blog.posts-page-size=5"])
class PostControllerMockMvcTests : AbstractIT() {
    @Test
    fun shouldGetPosts() {
        val result = mockMvcTester.get().uri("/api/posts").exchange()
        assertThat(result).hasStatus(OK)
            .bodyJson()
            .convertTo(PagedResult::class.java)
            .satisfies({ page: PagedResult<*> ->
                @Suppress("UNCHECKED_CAST")
                val pagedResult = page as PagedResult<PostDto>
                assertThat(pagedResult.data()).hasSize(5)
                assertThat(pagedResult.currentPageNo()).isEqualTo(1)
                assertThat(pagedResult.totalPages()).isEqualTo(2)
                assertThat(pagedResult.totalElements()).isEqualTo(9)
                assertThat(pagedResult.hasNextPage()).isTrue()
                assertThat(pagedResult.hasPreviousPage()).isFalse()
            })
    }

    @Test
    fun shouldSearchPosts() {
        val result = mockMvcTester.get().uri("/api/posts?query=spring").exchange()
        assertThat(result).hasStatus(OK)
            .bodyJson()
            .convertTo(PagedResult::class.java)
            .satisfies({ page: PagedResult<*> ->
                @Suppress("UNCHECKED_CAST")
                val pagedResult = page as PagedResult<PostDto>
                assertThat(pagedResult.data()).hasSize(5)
                assertThat(pagedResult.totalElements()).isEqualTo(7)
            })
    }

    @Test
    fun shouldGetPostBySlug() {
        val result = mockMvcTester.get()
            .uri("/api/posts/{slug}", "getting-started-with-spring-ai")
            .exchange()
        assertThat(result).hasStatus(OK)
            .bodyJson()
            .convertTo(PostDto::class.java)
            .satisfies({ post: PostDto ->
                assertThat(post.id()).isEqualTo(1)
                assertThat(post.title()).isEqualTo("Getting Started with Spring AI: Building LLM-Powered Applications")
                assertThat(post.slug()).isEqualTo("getting-started-with-spring-ai")
            })
    }

    @Test
    fun shouldCreatePostSuccessfully() {
        val result = mockMvcTester.post()
            .uri("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, userAuthToken)
            .content(
                """
                {
                  "title":"My New Post",
                  "slug":"my-new-post",
                  "content":"This is my test pose"
                }
                """.trimIndent(),
            )
            .exchange()
        assertThat(result).hasStatus(CREATED)
            .headers()
            .satisfies({ headers: HttpHeaders ->
                assertThat(headers.location).isNotNull
                assertThat(headers.location.toString()).endsWith("/api/posts/my-new-post")
            })
    }

    @Test
    fun shouldUpdatePostSuccessfully() {
        val result = mockMvcTester.put()
            .uri("/api/posts/{slug}", "gitops-argocd-continuous-delivery-kubernetes")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, adminAuthToken)
            .content(
                """
                {
                  "title":"GitOps with ArgoCD: Progressive Delivery on Kubernetes",
                  "slug":"gitops-argocd-progressive-delivery-kubernetes",
                  "content":"GitOps treats Git as the single source of truth for declarative infrastructure."
                }
                """.trimIndent(),
            )
            .exchange()
        assertThat(result).hasStatus(OK)
            .headers()
            .satisfies({ headers: HttpHeaders ->
                assertThat(headers.location).isNotNull
                assertThat(headers.location.toString())
                    .endsWith("/api/posts/gitops-argocd-progressive-delivery-kubernetes")
            })
    }
}
