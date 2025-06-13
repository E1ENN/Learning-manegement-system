package com.program.user_service.entities.support_entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<FieldError> errors;
}

