package com.travelbuddy.post.repository;

import com.travelbuddy.post.entities.DeletedPosts;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeletedPostsRepository extends MongoRepository<DeletedPosts, String> {

}
