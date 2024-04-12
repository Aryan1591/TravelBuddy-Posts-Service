package com.travelbuddy.post.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelbuddy.post.entities.Post;
import com.travelbuddy.post.exception.DuplicatePostException;
import com.travelbuddy.post.exception.PostNotExistException;
import com.travelbuddy.post.service.PostService;

import lombok.extern.slf4j.Slf4j; 

@RestController
@Slf4j
@RequestMapping("/post")
public class PostController 
{
	@Autowired
   private PostService service;
	

	@GetMapping("/all")
	public List<Post> retrieveAllPosts()
	{
		log.info("Request received for get all Post ");	
		return service.getAllPosts();
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> removePost(@PathVariable String id)
	{
		log.info("Request received for delete Post with id: {}",id);	
		try 
		{
			service.deletePost(id);
			return new ResponseEntity<>("Post has been Deleted Successfully",HttpStatus.OK);
		} 
		catch (PostNotExistException postNotExistException) 
		{
			return new ResponseEntity<>(postNotExistException.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/createpost")
	public ResponseEntity<?> generatePost(@RequestBody Post post)
	{
		log.info("Request received for create a Post");	
		try
		{
		   return new ResponseEntity<>(service.createPost(post),HttpStatus.OK);	
		}
		catch(DuplicatePostException duplicatePostException)
		{
		  return new ResponseEntity<>(duplicatePostException.getMessage(),HttpStatus.CONFLICT);  	
		}
		
		
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> renovatePost(@PathVariable String id,@RequestBody Post post)
	{
	        log.info("Request received for update Post with id: {}",id);	
		    try
		    {
			 return new ResponseEntity<>(service.updatePost(id,post),HttpStatus.OK);
		    }
		    catch(PostNotExistException postNotExistException)
		    {
		    	return new ResponseEntity<>(postNotExistException.getMessage(),HttpStatus.NOT_FOUND);
		    }
	}
	
	
	
	
}
