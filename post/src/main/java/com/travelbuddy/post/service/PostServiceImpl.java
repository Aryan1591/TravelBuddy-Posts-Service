package com.travelbuddy.post.service;

import com.travelbuddy.post.constants.Constants;
import com.travelbuddy.post.entities.Post;
import com.travelbuddy.post.exception.PostNotExistException;
import com.travelbuddy.post.model.Count;
import com.travelbuddy.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Value("${travelbuddy.deletedPostsCollection}")
    private String deletedPostsCollection;
    @Value("${travelbuddy.inactivePostsCollection}")
    private String inactivePostsCollection;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Post createPost(Post post) {
        // TODO Auto-generated method stub
        post.setStatus(Constants.Status.ACTIVE);
        String gender = restTemplate.getForObject("http://USER-MICROSERVICE/users/gender/" + post.getAdminName(), String.class);
        Count count = new Count(0, 0, 0);
        if ("Male".equals(gender)) {
            count.setMaleCount(1);
        } else if ("Female".equals(gender)) {
            count.setFemaleCount(1);
        } else {
            count.setOtherCount(1);
        }
        post.setCount(count);
        post.getUsers().add(post.getAdminName());
        return postRepository.save(post);
    }

    @Override
    public Boolean removeUserFromPost(String username, String postId) {
        Optional<Post> currentPost = postRepository.findById(postId);
        if (currentPost.isEmpty()) throw new PostNotExistException("Post doesn't exist");
        currentPost.get().setUsers(currentPost.get().getUsers().stream()
                .filter(usernames -> !usernames.equals(username)).collect(Collectors.toList()));
        String gender = restTemplate.getForObject("http://USER-MICROSERVICE/users/gender/" + username, String.class);
        if ("Male".equals(gender))
            currentPost.get().getCount().setMaleCount(currentPost.get().getCount().getMaleCount() - 1);
        else if ("Female".equals(gender)) {
            currentPost.get().getCount().setFemaleCount(currentPost.get().getCount().getFemaleCount() - 1);
        } else {
            currentPost.get().getCount().setOtherCount(currentPost.get().getCount().getOtherCount() - 1);
        }
        postRepository.save(currentPost.get());
        return Boolean.TRUE;
    }

    @Override
    public Post updateStatusToInactiveAndMoveToInactiveCollection(String postId) {
        Optional<Post> currentPost = postRepository.findById(postId);
        if (currentPost.isEmpty()) throw new PostNotExistException("Post doesn't exist");
        Post post = currentPost.get();
        post.setStatus(Constants.Status.INACTIVE);
        mongoTemplate.save(post, inactivePostsCollection);
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
        mongoTemplate.save(post.get(), deletedPostsCollection);
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
