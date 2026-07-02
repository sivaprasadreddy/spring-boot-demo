package com.sivalabs.blog.posts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    @Mapping(target = "createdByUserName", source = "createdBy.name")
    PostDto toPostDto(Post p);

    CommentDto toCommentDto(Comment cmd);
}
