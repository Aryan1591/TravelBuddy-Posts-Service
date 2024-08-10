package com.travelbuddy.post.service;

import com.travelbuddy.post.constants.Constants;
import com.travelbuddy.post.entities.Post;
import com.travelbuddy.post.exception.PostNotExistException;
import com.travelbuddy.post.feign.ChatServiceFeignClient;
import com.travelbuddy.post.feign.UserServiceFeignClient;
import com.travelbuddy.post.model.Count;
import com.travelbuddy.post.repository.PostRepository;
import com.travelbuddy.post.utils.EventsFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
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
    @Autowired
    private UserServiceFeignClient userServiceFeignClient;
    @Autowired
    private ChatServiceFeignClient chatServiceFeignClient;

    @Override
    public Post createPost(Post post) {
        post.setStatus(Constants.Status.ACTIVE);
        String gender = userServiceFeignClient.getGenderFromUsername(post.getAdminName());
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
        Post savedPost = postRepository.save(post);
        chatServiceFeignClient.buildChatRoom(savedPost.getId());
        userServiceFeignClient.addPostIdToUserBucket(post.getAdminName(), savedPost.getId());
        return savedPost;
    }

    @Override
    public Boolean removeUserFromPost(String username, String postId) {
        Optional<Post> currentPost = postRepository.findById(postId);
        if (currentPost.isEmpty()) throw new PostNotExistException("Post doesn't exist");
        currentPost.get().setUsers(currentPost.get().getUsers().stream()
                .filter(usernames -> !usernames.equals(username)).collect(Collectors.toList()));
        String gender = userServiceFeignClient.getGenderFromUsername(username);
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
    public String addUserToPost(final String username, final String postId) {
        Post existingPost = postRepository.findById(postId).orElse(null);
        if (null == existingPost)
            throw new PostNotExistException("Post doesn't exist");
        List<String> existingMembers = existingPost.getUsers();
        existingMembers.add(username);
        existingPost.setUsers(existingMembers);
        /*
           TODO
           Assuming user always exists , need to handle exceptions later
         */
        String gender = userServiceFeignClient.getGenderFromUsername(username);
        if ("Male".equals(gender)) {
            existingPost.getCount().setMaleCount(existingPost.getCount().getMaleCount() + 1);
        } else if ("Female".equals(gender)) {
            existingPost.getCount().setFemaleCount(existingPost.getCount().getFemaleCount() + 1);
        } else {
            existingPost.getCount().setOtherCount(existingPost.getCount().getOtherCount() + 1);
        }
        postRepository.save(existingPost);
        return "User has been successfully added";
    }

    @Override
    public Post updatePost(String id, Post updatedPost) throws PostNotExistException {
        // TODO Auto-generated method stub
        Post existingPost = postRepository.findById(id).orElse(null);
        if (Objects.isNull(existingPost)) {
            throw new PostNotExistException("This post doesn't exists,please check the id: " + id);
        }
        //filter the events since dates got changed
        if ((!updatedPost.getStartDate().equals(existingPost.getStartDate())) || (!updatedPost.getEndDate().equals(existingPost.getEndDate()))) {
            updatedPost.setEvents(EventsFilter.filterEvents(updatedPost.getStartDate(), updatedPost.getEndDate(), updatedPost.getEvents()));
        }
        return postRepository.save(updatedPost);
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
