package com.travelbuddy.post.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "UserService")
public interface UserServiceFeignClient {

    @GetMapping("/users/gender/{username}")
    public String getGenderFromUsername(@PathVariable String username);

    @PostMapping("/userPosts/{username}/posts/add")
    public String addPostIdToUserBucket(@PathVariable String username, @RequestParam String postId);

    @DeleteMapping("userPosts/{username}/posts/remove")
    public String removePostIdFromUserBucket(@PathVariable String username, @RequestParam String postId);
}
