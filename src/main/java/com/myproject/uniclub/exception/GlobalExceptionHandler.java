package com.myproject.uniclub.exception;

import com.myproject.uniclub.response.BaseResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({SaveFileException.class, ExpiredJwtException.class, RuntimeException.class}) // {} thể hiện nhiều exception.
    public ResponseEntity<?> handleException(Exception e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(500);
        response.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({AuthenException.class})
    public ResponseEntity<?> handleAuthenException(Exception e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(400);
        response.setMessage(e.getMessage());

        return ResponseEntity.status(403).body(response);
    }

}
