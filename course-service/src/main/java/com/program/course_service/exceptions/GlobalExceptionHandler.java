package com.program.course_service.exceptions;

import com.program.course_service.exceptions.exception_entities.ApiError;
import com.program.course_service.exceptions.exception_entities.FieldError;
import com.program.course_service.exceptions.exception_entities.FormatError;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage(), ex);
        List<FieldError> fieldsErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldError(fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .toList();
        ApiError apiError = new ApiError(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                fieldsErrors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<FormatError> handleEntityNotFoundException(EntityNotFoundException ex) {
        FormatError formatError = createFormatError(HttpStatus.NOT_FOUND, ex);
        return new ResponseEntity<>(formatError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<FormatError> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        FormatError formatError = createFormatError(HttpStatus.CONFLICT, ex);
        return new ResponseEntity<>(formatError, HttpStatus.CONFLICT);
    }

    private FormatError createFormatError(HttpStatus httpStatus, Exception ex) {
        return new FormatError(LocalDateTime.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                ex.getMessage());
    }


}
