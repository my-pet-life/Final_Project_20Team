package com.example.mypetlife.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

@Builder
@Data
public class ErrorResponse {

    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final String error;
    private final String message;

    public static ResponseEntity<ErrorResponse> createErrorResponse(ErrorCode errorCode) {

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .error(errorCode.getHttpStatus().name())
                        .message(errorCode.getMessage())
                        .build());
    }

    public static ResponseEntity<ErrorResponse> createErrorResponse(MethodArgumentNotValidException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .error(HttpStatus.BAD_REQUEST.name())
                        .message(e.getAllErrors().get(0).getDefaultMessage())
                        .build());
    }
}
