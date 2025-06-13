package com.program.user_service.services;

import com.program.user_service.entities.db_entities.Role;
import com.program.user_service.entities.db_entities.User;
import com.program.user_service.entities.support_entities.response_enitites.AuthenticationResponse;
import com.program.user_service.entities.support_entities.request_entities.LoginRequest;
import com.program.user_service.entities.support_entities.request_entities.RegisterRequest;
import com.program.user_service.repositories.UserRepository;
import com.program.user_service.security.auth.UserDetailsImpl;
import com.program.user_service.security.jwt.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationService {

    @NonNull private UserRepository userRepository;
    @NonNull private JwtService jwtService;
    @NonNull private PasswordEncoder passwordEncoder;
    @NonNull private RoleService roleService;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Register request received: {}", request);
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("USER"));
        log.info("Adding roles to user: {}", roles);
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash the password
        user.setRoles(roles);
        userRepository.save(user); // Save the user to the database
        log.info("User saved successfully with roles.");

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String jwt = jwtService.generateToken(userDetails);
        return new AuthenticationResponse(jwt);
    }

    @Transactional
    public AuthenticationResponse login(LoginRequest request) { // Принимайте LoginRequest
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password: " + request.getPassword());
        }

        UserDetailsImpl userDetails = new UserDetailsImpl(user); // Use UserDetailsImpl
        String jwt = jwtService.generateToken(userDetails);
        return new AuthenticationResponse(jwt);
    }
}
