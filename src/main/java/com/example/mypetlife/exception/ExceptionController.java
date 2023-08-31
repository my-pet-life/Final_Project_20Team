package com.example.mypetlife.exception;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    // Validation 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.error("MethodArgumentNotValidException 발생: {}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ErrorResponse.createErrorResponse(e);
    }

    // CustomException 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        log.error("CustomException 발생: {}", e.getErrorCode().getMessage());
        return ErrorResponse.createErrorResponse(e);
    }

    // ExpiredJwtException 예외 처리
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException e) {

        log.error("ExpiredJwtException 발생, 재로그인 필요");
        return ErrorResponse.createErrorResponse(e);
    }
}
