package com.example.mypetlife.jwt;

import com.example.mypetlife.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * 인가 관련 예외로 AccessDeniedException이 발생했을 때 AccessDeniedException 예외 처리를 하는 핸들러
 */
@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver resolver;

    public JwtAccessDeniedHandler(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        log.info("JwtAccessDeniedHandler 실행");

        // HandlerExceptionResolver에게 예외 처리를 위임
        resolver.resolveException(request, response, null, e);

//        ObjectMapper objectMapper = new ObjectMapper();
//
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("httpStatus", HttpServletResponse.SC_FORBIDDEN);
//        errorResponse.put("errorName", "AccessDeniedException");
//        errorResponse.put("message", e.getMessage() + ": 관리자만 접근 가능합니다.");
//
//        String responseBody = objectMapper.writeValueAsString(errorResponse);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("UTF-8");
//        response.setStatus(HttpStatus.FORBIDDEN.value());
//        response.getWriter().write(responseBody);
    }
}
