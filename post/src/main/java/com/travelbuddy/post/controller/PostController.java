package com.travelbuddy.post.controller;

import com.travelbuddy.post.entities.Post;
import com.travelbuddy.post.exception.PostNotExistException;
import com.travelbuddy.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService service;


    @GetMapping("/all")
    public List<Post> retrieveAllPosts() {
        log.info("Request received to get all Post ");
        return service.getAllPosts();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removePost(@PathVariable String id) {
        log.info("Request received to delete Post with id: {}", id);
        try {
            service.deletePost(id);
            return new ResponseEntity<>("Post has been Deleted Successfully", HttpStatus.OK);
        } catch (PostNotExistException postNotExistException) {
            return new ResponseEntity<>(postNotExistException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createpost")
    public ResponseEntity<?> generatePost(@RequestBody Post post) {
        log.info("Request received to create a Post");

        return new ResponseEntity<>(service.createPost(post), HttpStatus.OK);


    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> renovatePost(@PathVariable String id, @RequestBody Post post) {
        log.info("Request received to update Post with id: {}", id);
        try {
            return new ResponseEntity<>(service.updatePost(id, post), HttpStatus.OK);
        } catch (PostNotExistException postNotExistException) {
            return new ResponseEntity<>(postNotExistException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/removeUser/{username}/{postId}")
    public ResponseEntity<?> removeUserFromPost(@PathVariable String username, @PathVariable String postId) {
        log.info("Request received to remove user {} from post {}", username, postId);
        try {
            return new ResponseEntity<>(service.removeUserFromPost(username, postId), HttpStatus.OK);
        } catch (PostNotExistException postNotExistException) {
            return new ResponseEntity<>(postNotExistException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateStatusToInactive/{id}")
    public ResponseEntity<?> updateStatusToInactive(@PathVariable String postId) {
        log.info("Updating Status To Inactive for id {}", postId);
        try {
            return new ResponseEntity<>(service.updateStatusToInactiveAndMoveToInactiveCollection(postId), HttpStatus.OK);
        } catch (PostNotExistException postNotExistException) {
            return new ResponseEntity<>(postNotExistException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateStatusToLocked/{id}")
    public ResponseEntity<?> updateStatusToLocked(@PathVariable String postId) {
        log.info("Updating Status To Locked for id {}", postId);
        try {
            return new ResponseEntity<>(service.updateStatusToLocked(postId), HttpStatus.OK);
        } catch (PostNotExistException postNotExistException) {
            return new ResponseEntity<>(postNotExistException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
