package com.travelbuddy.post.service;

import com.travelbuddy.post.constants.Constants;
import com.travelbuddy.post.entities.Post;
import com.travelbuddy.post.exception.PostNotExistException;
import com.travelbuddy.post.mapper.CommonMapper;
import com.travelbuddy.post.repository.DeletedPostsRepository;
import com.travelbuddy.post.repository.InactivePostsRepository;
import com.travelbuddy.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    private final CommonMapper commonMapper = CommonMapper.INSTANCE;
    @Autowired
    private DeletedPostsRepository deletedPostRepository;

    @Autowired
    private InactivePostsRepository inactivePostRepository;

    @Override
    public Post createPost(Post post) {
        // TODO Auto-generated method stub
        post.setStatus(Constants.Status.ACTIVE);
        return postRepository.save(post);

    }

    @Override
    public Boolean removeUserFromPost(String username, String postId) {
        Optional<Post> currentPost = postRepository.findById(postId);
        if (currentPost.isEmpty()) throw new PostNotExistException("Post doesn't exist");
        currentPost.get().setUsers(currentPost.get().getUsers().stream().filter(usernames -> !usernames.equals(username)).collect(Collectors.toList()));
        postRepository.save(currentPost.get());
        return Boolean.TRUE;
    }

    @Override
    public Post updateStatusToInactiveAndMoveToInactiveCollection(String postId) {
        Optional<Post> currentPost = postRepository.findById(postId);
        if (currentPost.isEmpty()) throw new PostNotExistException("Post doesn't exist");
        Post post = currentPost.get();
        post.setStatus(Constants.Status.INACTIVE);
        inactivePostRepository.save(commonMapper.postToInactivePost(post));
        postRepository.deleteById(postId);
        return post;
    }

    @Override
    public Post updateStatusToLocked(String postId) {
        Optional<Post> currentPost = postRepository.findById(postId);
        if (currentPost.isEmpty()) throw new PostNotExistException("Post doesn't exist");
        Post post = currentPost.get();
        post.setStatus(Constants.Status.LOCKED);
        postRepository.save(post);
        return post;
    }

    @Override
    public Post updatePost(String id, Post post) throws PostNotExistException {
        // TODO Auto-generated method stub
        if (!postRepository.existsById(id)) {
            throw new PostNotExistException("the Post Doesn't Exits,Please check the id: " + id);
        }
        return postRepository.save(post);
    }

    @Override
    public void deletePost(String id) throws PostNotExistException {
        // TODO Auto-generated method stub
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new PostNotExistException(String.format("Post doesn't exists with id %s", id));
        }
        deletedPostRepository.save(commonMapper.postToDeletedPost(post.get()));
        postRepository.deleteById(id);

    }

    @Override
    public Post getPostById(String id) throws PostNotExistException {
        // TODO Auto-generated method stub
        Optional<Post> postDetails = postRepository.findById(id);
        if (postDetails.isEmpty()) {
            throw new PostNotExistException("The Post Doesn't Exits,Please check the id");

        }

        return postDetails.get();
    }

    @Override
    public List<Post> getAllPosts() {
        // TODO Auto-generated method stub
        return postRepository.findAll();
    }


}
