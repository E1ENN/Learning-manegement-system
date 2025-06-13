package com.program.course_service.feign_config;

import com.program.course_service.entities.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/users/profile/{userId}")
    Optional<UserResponse> getUser(@PathVariable("userId") Long userId);
}
