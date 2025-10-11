package com.sivalabs.blog.content.core;

import com.sivalabs.blog.shared.entities.PostEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostEventRepository extends MongoRepository<PostEvent, String> {}
