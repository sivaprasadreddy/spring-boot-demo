package com.sivalabs.blog.posts;

import com.sivalabs.blog.shared.PagedResult;
import com.sivalabs.blog.shared.ResourceNotFoundException;
import com.sivalabs.blog.users.UserContextUtils;
import com.sivalabs.blog.users.UsersAPI;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Posts API")
class PostController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    private final UserContextUtils userContextUtils;
    private final UsersAPI usersAPI;

    PostController(PostService postService, UserContextUtils userContextUtils, UsersAPI usersAPI) {
        this.postService = postService;
        this.userContextUtils = userContextUtils;
        this.usersAPI = usersAPI;
    }

    @GetMapping
    PagedResult<PostDto> findPosts(
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        log.info("Get posts by page='{}' and query='{}'", page, query);
        return postService.findPosts(page, query);
    }

    @GetMapping("/{slug}")
    ResponseEntity<PostDto> getPostBySlug(@PathVariable(value = "slug") String slug) {
        log.info("Get post by slug='{}'", slug);
        var post = postService
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        return ResponseEntity.ok(post);
    }

    @PostMapping("")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<Void> createPost(@Valid @RequestBody Post post) {
        var loginUserId = userContextUtils.getCurrentUserIdOrThrow();
        var user = usersAPI.findById(loginUserId).orElseThrow();
        var slug = post.getSlug();
        log.info("Creating a new post with slug: '{}'", slug);
        var newPost = new Post(post.getTitle(), post.getSlug(), post.getContent(), user);
        this.postService.createPost(newPost);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath(null)
                .path("/api/posts/{slug}")
                .buildAndExpand(slug)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{slug}")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<Void> updatePost(@PathVariable("slug") String slug,
                                    @Valid @RequestBody Post post) {
        log.info("Updating post with slug: '{}'", slug);
        PostDto postDto = postService
                .findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));

        this.postService.updatePost(postDto.id(), post);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath(null)
                .path("/api/posts/{slug}")
                .buildAndExpand(post.getSlug())
                .toUri();
        return ResponseEntity.status(HttpStatus.OK).location(location).build();
    }
}
