package com.sivalabs.blog.posts;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface PostMapper {
    @Mapping(target = "author", source = "createdBy.name")
    PostDto toPostDto(Post p);
}
