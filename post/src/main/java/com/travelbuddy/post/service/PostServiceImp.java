package com.travelbuddy.post.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelbuddy.post.entities.Post;
import com.travelbuddy.post.exception.DuplicatePostException;
import com.travelbuddy.post.exception.PostNotExistException;
import com.travelbuddy.post.repository.PostRepository;

@Service
public class PostServiceImp implements PostService 
{

	@Autowired
	private PostRepository repository;
	
	@Override
	public Post createPost(Post post) throws DuplicatePostException
	{
		// TODO Auto-generated method stub
		//return repository.save(post);
		Optional<Post> postDetails =repository.findById(post.getId());
		if(!postDetails.isEmpty())
		{
			throw new DuplicatePostException("Post Already Exists,choose different id");
		}
		repository.save(post);
		return post;
	}

	@Override
	public Post updatePost(String id,Post post) throws PostNotExistException
	{
		// TODO Auto-generated method stub
		if(!repository.existsById(id))
		{
			throw new PostNotExistException("the Post Doesn't Exits,Please check the id: "+id);
		}
		return repository.save(post);
	}

	@Override
	public void deletePost(String id) throws PostNotExistException 
	{
		// TODO Auto-generated method stub
		if(!repository.existsById(id))
		{
			throw new PostNotExistException("the Post Doesn't Exits,Please check the id: "+id);
		}
		else
		{
			repository.deleteById(id);
		}
	      	
	}

	@Override
	public Post getPostById(String id) throws PostNotExistException 
	{
		// TODO Auto-generated method stub
		Optional<Post> postDetails=repository.findById(id);
		if(postDetails.isEmpty())
		{
			throw new PostNotExistException("The Post Doesn't Exits,Please check the id");
			
		}
		
		return postDetails.get();
	}

	@Override
	public List<Post> getAllPosts() 
	{
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	
}
