package com.program.user_service.controllers;

import com.program.user_service.entities.support_entities.request_entities.ChangePasswordRequest;
import com.program.user_service.entities.support_entities.request_entities.UpdateRequest;
import com.program.user_service.entities.support_entities.response_enitites.UserResponse;
import com.program.user_service.security.auth.UserDetailsImpl;
import com.program.user_service.services.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/users/profile")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class UserController {

    @NonNull
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long userId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!userId.equals(userDetails.getUserId())) {
            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long userId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @Valid @RequestBody UpdateRequest updateRequest) throws AccessDeniedException {
        if (!userId.equals(userDetails.getUserId())) {
            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
        }
        UserResponse response = userService.updateUser(userId, updateRequest);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> changePassword(@PathVariable("id") Long userId,
                                               @Valid @RequestBody ChangePasswordRequest request) throws AccessDeniedException {
        userService.changePassword(userId, request);
        return ResponseEntity.noContent().build();
    }
}
