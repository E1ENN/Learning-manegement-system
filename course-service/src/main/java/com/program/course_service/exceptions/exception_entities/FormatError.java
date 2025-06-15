package com.program.course_service.exceptions.exception_entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormatError {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
}
