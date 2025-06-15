package com.program.user_service.controllers.without_view;

import com.program.user_service.entities.support_entities.request_entities.LoginRequest;
import com.program.user_service.entities.support_entities.request_entities.RegisterRequest;
import com.program.user_service.entities.support_entities.response_enitites.AuthenticationResponse;
import com.program.user_service.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/non-view/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationController {

    @NonNull
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request received: {}", request);
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request received: {}", request);
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
