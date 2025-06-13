package com.program.user_service.entities.support_entities.request_entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "The name cannot be empty")
    @Size(min = 2, max = 12, message = "The name must be in the range of 2 to 12 characters")
    private String firstName;

    @NotBlank(message = "The last name cannot be empty")
    @Size(min = 2, max = 12, message = "The last name must be in the range of 2 to 12 characters")
    private String lastName;

    @NotBlank(message = "The email cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "The password cannot be empty")
    @Size(min = 8, max = 30, message = "The password must be in the range of 8 to 30 characters")
    private String password;
}
