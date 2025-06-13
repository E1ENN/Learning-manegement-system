package com.program.user_service.entities.support_entities.response_enitites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private Set<String> roles;
}
