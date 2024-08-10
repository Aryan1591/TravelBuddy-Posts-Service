package com.travelbuddy.post.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "UserService")
public interface UserServiceFeignClient {

    @GetMapping("/users/gender/{username}")
    public String getGenderFromUsername(@PathVariable String username);

    @PostMapping("/users/{username}/posts/add")
    public String addPostIdToUserBucket(@PathVariable String username, @RequestBody String postId);
}
