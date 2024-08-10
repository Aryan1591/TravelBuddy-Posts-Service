package com.travelbuddy.post.service;

import com.travelbuddy.post.entities.Post;
import com.travelbuddy.post.exception.PostNotExistException;

import java.util.List;

public interface PostService {
    public Post updatePost(String id, Post post) throws PostNotExistException;

    public void deletePost(String id) throws PostNotExistException;

    public Post getPostById(String id) throws PostNotExistException;

    public List<Post> getAllPosts();

    public Post createPost(Post post);

    Boolean removeUserFromPost(String username, String postId);

    Post updateStatusToInactiveAndMoveToInactiveCollection(String postId);

    Post updateStatusToLocked(String postId);

    String addUserToPost(String username, String postId);

}
