package com.travelbuddy.post.service;

import java.util.List;
import java.util.Optional;

import com.travelbuddy.post.entities.Post;
import com.travelbuddy.post.exception.DuplicatePostException;
import com.travelbuddy.post.exception.PostNotExistException;

public interface PostService 
{


	public Post updatePost(String id, Post post) throws PostNotExistException;
	public void deletePost(String id) throws PostNotExistException;
	public Post getPostById(String id) throws PostNotExistException;
	public List<Post> getAllPosts();
	public Post createPost(Post post) throws DuplicatePostException;

	
	
}
