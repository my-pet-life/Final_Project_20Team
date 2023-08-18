package com.example.mypetlife.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return ErrorResponse.createErrorResponse(e.getErrorCode());
    }
}
