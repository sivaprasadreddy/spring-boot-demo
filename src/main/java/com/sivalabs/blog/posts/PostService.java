package com.sivalabs.blog.posts;

import com.sivalabs.blog.ApplicationProperties;
import com.sivalabs.blog.shared.BadRequestException;
import com.sivalabs.blog.shared.ResourceNotFoundException;
import com.sivalabs.blog.shared.PagedResult;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ApplicationProperties properties;
    private final PostMapper postMapper;

    PostService(
            PostRepository postRepository,
            ApplicationEventPublisher eventPublisher,
            ApplicationProperties properties,
            PostMapper postMapper) {
        this.postRepository = postRepository;
        this.eventPublisher = eventPublisher;
        this.properties = properties;
        this.postMapper = postMapper;
    }

    @Transactional(readOnly = true)
    public PagedResult<PostDto> findPosts(Integer pageNo, String query) {
        Pageable pageable = this.getPageRequest(pageNo);
        Page<PostDto> posts;
        if (query == null || query.trim().isEmpty()) {
            posts = postRepository.findPosts(pageable).map(postMapper::toPostDto);
        } else {
            posts = postRepository.searchPosts("%" + query.toLowerCase() + "%", pageable).map(postMapper::toPostDto);
        }
        return PagedResult.from(posts);
    }

    @Transactional(readOnly = true)
    public Optional<PostDto> findBySlug(String slug) {
        return postRepository.findBySlug(slug).map(postMapper::toPostDto);
    }

    @Transactional
    public void createPost(Post post) {
        if(this.isPostSlugExists(post.getSlug())) {
            throw new BadRequestException("Post with slug '" + post.getSlug() + "' already exists");
        }
        post.setId(null);
        postRepository.save(post);
        var event = new PostPublishedEvent(post.getTitle(), post.getSlug(), post.getContent(), LocalDateTime.now());
        eventPublisher.publishEvent(event);
    }

    @Transactional
    public void updatePost(Long postId, Post post) {
        var entity = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + post.getId() + " not found"));
        var updatedSlug = post.getSlug();
        Optional<PostDto> postBySlug = this.findBySlug(updatedSlug);
        if (postBySlug.isPresent() && !Objects.equals(postBySlug.get().id(), entity.getId())) {
            throw new BadRequestException("Post with slug '" + updatedSlug + "' already exists");
        }
        entity.setTitle(post.getTitle());
        entity.setSlug(post.getSlug());
        entity.setContent(post.getContent());
        postRepository.save(entity);
    }

    private boolean isPostSlugExists(String slug) {
        return postRepository.existsBySlug(slug);
    }

    private Pageable getPageRequest(Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        int pageSize = properties.postsPageSize();
        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        return PageRequest.of(pageNo - 1, pageSize, sort);
    }
}
