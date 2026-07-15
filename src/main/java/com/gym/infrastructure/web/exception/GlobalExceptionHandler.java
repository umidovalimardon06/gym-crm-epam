package com.gym.infrastructure.web.exception;

import com.gym.application.exception.AuthenticationException;
import com.gym.application.exception.InvalidStateException;
import com.gym.application.exception.NotFoundException;
import com.gym.application.exception.TraineeDeletionException;
import com.gym.infrastructure.web.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException e, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED,e.getMessage(),req);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException e,
                                                        HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, e.getMessage(), req);
    }

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidStateException e,
                                                            HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, e.getMessage(), req);
    }

    @ExceptionHandler(TraineeDeletionException.class)
    public ResponseEntity<ErrorResponse> handleDelete(TraineeDeletionException e,
                                                      HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, e.getMessage(), req);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadArg(IllegalArgumentException e,
                                                      HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e,
                                                          HttpServletRequest req) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, message, req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e,
                                                          HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", req);
    }


    private ResponseEntity<ErrorResponse> build(HttpStatus status,
                                                String message,
                                                HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(status).body(body);
    }
}