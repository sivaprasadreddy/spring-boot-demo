package com.sivalabs.blog.content.core;

import com.sivalabs.blog.ApplicationProperties;
import com.sivalabs.blog.content.core.models.CreateCommentCmd;
import com.sivalabs.blog.content.core.models.CreatePostCmd;
import com.sivalabs.blog.content.core.models.UpdatePostCmd;
import com.sivalabs.blog.content.events.BlogEventPublisher;
import com.sivalabs.blog.content.events.PostPublishedEvent;
import com.sivalabs.blog.shared.entities.Comment;
import com.sivalabs.blog.shared.entities.Post;
import com.sivalabs.blog.shared.exceptions.ResourceNotFoundException;
import com.sivalabs.blog.shared.models.PagedResult;
import com.sivalabs.blog.users.UsersAPI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UsersAPI usersAPI;
    private final BlogEventPublisher blogEventPublisher;
    private final ApplicationProperties properties;

    PostService(
            PostRepository postRepository,
            CommentRepository commentRepository,
            UsersAPI usersAPI,
            BlogEventPublisher blogEventPublisher,
            ApplicationProperties properties) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.usersAPI = usersAPI;
        this.blogEventPublisher = blogEventPublisher;
        this.properties = properties;
    }

    @Transactional(readOnly = true)
    public PagedResult<Post> findPosts(Integer pageNo) {
        Pageable pageable = this.getPageRequest(pageNo);
        Page<Post> posts = postRepository.findPosts(pageable);
        return PagedResult.from(posts);
    }

    @Transactional(readOnly = true)
    public PagedResult<Post> searchPosts(String query, Integer pageNo) {
        Pageable pageable = this.getPageRequest(pageNo);
        Page<Post> posts = postRepository.searchPosts("%" + query.toLowerCase() + "%", pageable);
        return PagedResult.from(posts);
    }

    @Transactional(readOnly = true)
    public List<Post> findPostsCreatedBetween(LocalDateTime start, LocalDateTime end) {
        return postRepository.findByCreatedDate(start, end);
    }

    @Transactional(readOnly = true)
    public Optional<Post> findPostBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    @Transactional(readOnly = true)
    public Optional<Post> findPostById(Long postId) {
        return postRepository.findPostById(postId);
    }

    @Transactional
    public void createPost(CreatePostCmd cmd) {
        var user = usersAPI.findUserById(cmd.createdBy()).orElseThrow();

        var entity = new Post();
        entity.setTitle(cmd.title());
        entity.setSlug(cmd.slug());
        entity.setContent(cmd.content());
        entity.setCreatedBy(user);
        postRepository.save(entity);

        var event =
                new PostPublishedEvent(entity.getTitle(), entity.getSlug(), entity.getContent(), LocalDateTime.now());
        blogEventPublisher.publish(event);
    }

    @Transactional
    public void updatePost(UpdatePostCmd cmd) {
        var entity = postRepository
                .findById(cmd.id())
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + cmd.id() + " not found"));
        entity.setTitle(cmd.title());
        entity.setSlug(cmd.slug());
        entity.setContent(cmd.content());
        postRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public boolean isPostSlugExists(String slug) {
        return postRepository.existsBySlug(slug);
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Transactional
    public void createComment(CreateCommentCmd cmd) {
        var post = postRepository.getReferenceById(cmd.postId());
        var entity = new Comment();
        entity.setName(cmd.name());
        entity.setEmail(cmd.email());
        entity.setContent(cmd.content());
        entity.setPost(post);
        commentRepository.save(entity);
    }

    private Pageable getPageRequest(Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        int pageSize = properties.postsPerPage();
        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        return PageRequest.of(pageNo - 1, pageSize, sort);
    }
}
