package com.sivalabs.blog.content.core;

import com.sivalabs.blog.content.core.models.CommentDto;
import com.sivalabs.blog.content.core.models.PostDto;
import com.sivalabs.blog.shared.entities.Comment;
import com.sivalabs.blog.shared.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    @Mapping(target = "createdByUserName", source = "createdBy.name")
    PostDto toPostDto(Post p);

    CommentDto toCommentDto(Comment cmd);
}
