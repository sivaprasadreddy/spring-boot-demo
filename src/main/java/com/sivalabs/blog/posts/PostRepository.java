package com.sivalabs.blog.posts;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.createdBy where p.slug = :slug")
    Optional<Post> findBySlug(@Param("slug") String slug);

    @Query("select p from Post p join fetch p.createdBy")
    Page<Post> findPosts(Pageable pageable);

    @Query("""
        select p from Post p join fetch p.createdBy
        where lower(p.title) like :query or lower(p.content) like :query
    """)
    Page<Post> searchPosts(@Param("query") String query, Pageable pageable);

    boolean existsBySlug(String slug);
}
