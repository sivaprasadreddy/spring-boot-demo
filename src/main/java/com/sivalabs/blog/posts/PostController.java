package com.sivalabs.blog.posts;

import com.sivalabs.blog.shared.BadRequestException;
import com.sivalabs.blog.shared.ResourceNotFoundException;
import com.sivalabs.blog.shared.PagedResult;
import com.sivalabs.blog.users.UserContextUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Posts API")
class PostController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    private final BlogMapper blogMapper;

    PostController(PostService postService, BlogMapper blogMapper) {
        this.postService = postService;
        this.blogMapper = blogMapper;
    }

    @GetMapping("")
    PagedResult<PostDto> findPosts(
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "1") Integer page) {
        log.info("Get posts by page='{}' and query='{}'", page, query);
        if (query == null || query.trim().isEmpty()) {
            return postService.findPosts(page).map(blogMapper::toPostDto);
        }
        return postService.searchPosts(query, page).map(blogMapper::toPostDto);
    }

    @GetMapping("/{slug}")
    ResponseEntity<PostDto> getPostBySlug(@PathVariable(value = "slug") String slug) {
        log.info("Get post by slug='{}'", slug);
        var post = postService
                .findPostBySlug(slug)
                .map(blogMapper::toPostDto)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{slug}/comments")
    List<CommentDto> getPostComments(@PathVariable(value = "slug") String slug) {
        log.info("Get post comments by slug='{}'", slug);
        PostDto postDto = postService
                .findPostBySlug(slug)
                .map(blogMapper::toPostDto)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        return postService.getCommentsByPostId(postDto.id()).stream()
                .map(blogMapper::toCommentDto)
                .toList();
    }

    @PostMapping("/{slug}/comments")
    ResponseEntity<Void> createComment(
            @PathVariable(value = "slug") String slug, @Valid @RequestBody CreateCommentPayload payload) {
        log.info("Create comment for post with slug: '{}'", slug);
        PostDto postDto = postService
                .findPostBySlug(slug)
                .map(blogMapper::toPostDto)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        var createdCommentCmd = new CreateCommentCmd(payload.name, payload.email, payload.content, postDto.id());
        postService.createComment(createdCommentCmd);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    record CreateCommentPayload(
            @NotEmpty(message = "Name is required") String name,

            @NotEmpty(message = "Email is required") @Email(message = "Invalid email address") String email,

            @NotEmpty(message = "Content is required") String content) {}

    @PostMapping("")
    @SecurityRequirement(name = "Bearer")
    ResponseEntity<Void> createPost(@Valid @RequestBody PostPayload postPayload) {
        var loginUserId = UserContextUtils.getCurrentUserIdOrThrow();
        var slug = postPayload.slug();
        log.info("Creating a new post with slug: '{}'", slug);
        var cmd = new CreatePostCmd(postPayload.title(), slug, postPayload.content(), loginUserId);
        this.postService.createPost(cmd);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath(null)
                .path("/api/posts/{slug}")
                .buildAndExpand(slug)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{slug}")
    @SecurityRequirement(name = "Bearer")
    ResponseEntity<Void> updatePost(@PathVariable("slug") String slug, @Valid @RequestBody PostPayload postPayload) {
        log.info("Updating post with slug: '{}'", slug);
        PostDto postDto = postService
                .findPostBySlug(slug)
                .map(blogMapper::toPostDto)
                .orElseThrow(() -> new ResourceNotFoundException("Post with slug '" + slug + "' not found"));
        var updatedSlug = postPayload.slug();
        Optional<PostDto> postBySlug = postService.findPostBySlug(updatedSlug).map(blogMapper::toPostDto);
        if (postBySlug.isPresent() && !Objects.equals(postBySlug.get().id(), postDto.id())) {
            throw new BadRequestException("Post with slug '" + updatedSlug + "' already exists");
        }
        var cmd = new UpdatePostCmd(postDto.id(), postPayload.title(), updatedSlug, postPayload.content());
        this.postService.updatePost(cmd);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath(null)
                .path("/api/posts/{slug}")
                .buildAndExpand(updatedSlug)
                .toUri();
        return ResponseEntity.status(HttpStatus.OK).location(location).build();
    }

    record PostPayload(
            @NotEmpty(message = "Title is required") String title,
            @NotEmpty(message = "Slug is required") String slug,
            @NotEmpty(message = "Content is required") String content) {}
}
