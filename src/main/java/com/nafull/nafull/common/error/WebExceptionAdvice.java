package com.nafull.nafull.common.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebExceptionAdvice {
    @ExceptionHandler(WebException.class)
    public ResponseEntity<ErrorResponse> handle(WebException exception) {
        return ResponseEntity
                .status(exception.getCode().getHttpStatus())
                .body(new ErrorResponse(exception.getMessage(), exception.getCode()));
    }
}
