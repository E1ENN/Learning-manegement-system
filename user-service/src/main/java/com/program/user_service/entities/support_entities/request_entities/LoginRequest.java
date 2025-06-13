package com.program.user_service.entities.support_entities.request_entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "The email cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "The password cannot be empty")
    @Size(min = 8, max = 30, message = "The password must be in the range of 2 to 30 characters")
    private String password;
}
