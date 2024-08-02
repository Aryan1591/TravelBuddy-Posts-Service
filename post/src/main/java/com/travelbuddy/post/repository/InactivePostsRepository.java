package com.travelbuddy.post.repository;

import com.travelbuddy.post.entities.InactivePosts;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InactivePostsRepository extends MongoRepository<InactivePosts, String> {

}
