package com.jellybrains.quietspace_backend_ms.userservice.controller;

import com.jellybrains.quietspace.common_service.exception.*;
import com.jellybrains.quietspace.common_service.model.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(RuntimeException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .status(status.name())
                        .message(e.getMessage())
                        .timestamp(new Date())
                        .build());
    }

    @ExceptionHandler(TransactionSystemException.class)
    ResponseEntity<?> handleJPAViolations(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        if (exception.getCause().getCause() instanceof ConstraintViolationException violationException) {

            List<Map<String, String>> errors = violationException.getConstraintViolations().stream()
                    .map(constraintViolation -> {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put(constraintViolation.getPropertyPath().toString(),
                                constraintViolation.getMessage());
                        return errorMap;
                    }).collect(Collectors.toList());

            return responseEntity.body(errors);
        }

        return responseEntity.build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> handleBindErrors(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errorList = exception.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                }).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(RuntimeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(ErrorResponse.builder()
                .status(status.name())
                .message(e.getMessage())
                .build(), status);
    }


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(RuntimeException e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .status(status.name())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(RuntimeException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .status(status.name())
                        .message(e.getMessage())
                        .timestamp(new Date())
                        .build());
    }


    @ExceptionHandler(CustomDataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomDataNotFoundExceptions(RuntimeException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .status(status.name())
                        .message(e.getMessage())
                        .timestamp(new Date())
                        .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundExceptions(RuntimeException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .status(status.name())
                        .message(e.getMessage())
                        .timestamp(new Date())
                        .build());
    }

    @ExceptionHandler(CustomParameterConstraintException.class)
    public ResponseEntity<ErrorResponse> handleCustomParameterConstraintExceptions(RuntimeException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .status(status.name())
                        .message(e.getMessage())
                        .timestamp(new Date())
                        .build());
    }

    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<ErrorResponse> handleCustomErrorExceptions(CustomErrorException e) {
        HttpStatus status = e.getStatus();
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .status(status.name())
                        .message(e.getMessage())
                        .timestamp(new Date())
                        .build());
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorResponse> handleMailingException(RuntimeException e) {
        HttpStatus status = INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .status(status.name())
                        .message(e.getMessage())
                        .timestamp(new Date())
                        .build());
    }

}
