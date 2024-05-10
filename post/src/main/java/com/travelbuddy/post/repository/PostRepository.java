package com.travelbuddy.post.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.travelbuddy.post.entities.Post;

public interface PostRepository extends MongoRepository<Post, String> {

}
