package com.marbor.customauthentication.security;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
   private final HttpStatus httpStatus;

    public CustomException(String message, HttpStatus internalServerError) {
        super(message);
        this.httpStatus = internalServerError;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
