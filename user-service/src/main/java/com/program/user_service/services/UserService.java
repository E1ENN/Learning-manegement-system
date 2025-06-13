package com.program.user_service.services;

import com.program.user_service.entities.db_entities.Role;
import com.program.user_service.entities.db_entities.User;
import com.program.user_service.entities.support_entities.request_entities.ChangePasswordRequest;
import com.program.user_service.entities.support_entities.request_entities.UpdateRequest;
import com.program.user_service.entities.support_entities.response_enitites.CourseResponse;
import com.program.user_service.entities.support_entities.response_enitites.UserResponse;
import com.program.user_service.repositories.CourseServiceClient;
import com.program.user_service.repositories.UserRepository;
import com.program.user_service.security.auth.UserDetailsImpl;
import com.program.user_service.security.jwt.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    @NonNull
    private UserRepository userRepository;
    @NonNull
    private PasswordEncoder passwordEncoder;
    @NonNull
    private Validator validator;
    @NonNull
    private JwtService jwtService;
    @NonNull
    private CourseServiceClient courseServiceClient;

    @Transactional
    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " not found"));
        return new UserResponse(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName)
                        .collect(Collectors.toSet()));
    }

    @Transactional
    public User getUserFromDB(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " not found"));

    }

    @Transactional
    public List<CourseResponse> getCoursesByTeacher(Long teacherId) {
        return courseServiceClient.findCoursesByTeacher(teacherId);
    }

    @Transactional
    public UserResponse updateUser(Long userId, UpdateRequest request) throws AccessDeniedException {
        authorizationAndAuthentication(userId);
        User user = getUserFromDB(userId);
        validationUpdateRequestData(user, request);
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
            SecurityContextHolder.clearContext();
        }
        userRepository.save(user);
        return new UserResponse(user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName)
                        .collect(Collectors.toSet()));
    }

    private String updateJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String newJwt = jwtService.generateToken(userDetails);
        updateSecurityContext(userDetails);
        return newJwt;
    }

    private void updateSecurityContext(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken newAuthToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuthToken);
    }

    private void validationUpdateRequestData(User user, UpdateRequest request) {
        Set<ConstraintViolation<UpdateRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<UpdateRequest> violation : violations)
                sb.append(violation.getMessage());
            throw new IllegalArgumentException("Invalid UpdateRequest data: " + sb);
        }
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail()))
            throw new IllegalArgumentException("Email already exists");
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) throws AccessDeniedException {
        authorizationAndAuthentication(userId);
        User user = getUserFromDB(userId);
        passwordsCorrectness(user, request);
        String encodedNewPassword = passwordEncoder.encode(request.getChangedPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
        SecurityContextHolder.clearContext();
    }

    private void authorizationAndAuthentication(Long userId) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl authUser = (UserDetailsImpl) authentication.getPrincipal();
        if (!authUser.getUserId().equals(userId) && authentication.getAuthorities()
                .stream().noneMatch(role -> role.getAuthority().equals("ADMIN")))
            throw new AccessDeniedException("You are not authorized to change this user password");
    }

    private void passwordsCorrectness(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
            throw new IllegalArgumentException("Invalid current password");
        if (request.getChangedPassword() == null || request.getChangedPassword().length() < 8
                || request.getChangedPassword().length() > 30)
            throw new IllegalArgumentException("The password must be in the range of 2 to 30 characters");
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}

