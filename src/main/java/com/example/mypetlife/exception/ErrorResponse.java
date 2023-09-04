package com.example.mypetlife.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;


import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ErrorResponse {

    private final LocalDateTime localDateTime = LocalDateTime.now();
    private String errorName;
    private String httpStatus;
    private String message;

    /*
     * 커스텀 예외
     */
    public static ResponseEntity<ErrorResponse> createErrorResponse(CustomException e) {

        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ErrorResponse.builder()
                        .errorName(e.getErrorCode().name())
                        .httpStatus(e.getErrorCode().getHttpStatus().name())
                        .message(e.getErrorCode().getMessage())
                        .build());
    }

    /*
     * MethodArgumentNotValidException
     */
    public static ResponseEntity<ErrorResponse> createErrorResponse(MethodArgumentNotValidException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errorName("MethodArgumentNotValidException")
                        .httpStatus(HttpStatus.BAD_REQUEST.name())
                        .message(e.getAllErrors().get(0).getDefaultMessage())
                        .build());
    }

    /*
     * ExpiredJwtException
     */
    public static ResponseEntity<ErrorResponse> createErrorResponse(ExpiredJwtException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .errorName("ExpiredJwtException")
                        .httpStatus(HttpStatus.UNAUTHORIZED.name())
                        .message("Refresh Token이 만료되었습니다. 재로그인이 필요합니다.")
                        .build());
    }

    /*
     * AuthenticationException
     */
    public static ResponseEntity<ErrorResponse> createErrorResponse(AuthenticationException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .errorName("AuthenticationException")
                        .httpStatus(HttpStatus.UNAUTHORIZED.name())
                        .message(e.getMessage())
                        .build());
    }

    /*
     * AccessDeniedException
     */
    public static ResponseEntity<ErrorResponse> createErrorResponse(AccessDeniedException e) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder()
                        .errorName("AccessDeniedException")
                        .httpStatus(HttpStatus.FORBIDDEN.name())
                        .message("관리자만 접근 가능합니다.")
                        .build());
    }

    public static ResponseEntity<ErrorResponse> createErrorResponse(JwtException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .errorName("JwtException")
                        .httpStatus(HttpStatus.UNAUTHORIZED.name())
                        .message(e.getMessage())
                        .build());
    }
}
