package com.qbrainx_recruitment.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidTokenRequestException extends RuntimeException {

    private static final long serialVersionUID = 7626222413320668104L;

    private final String tokenType;
    private final String token;
    private final String message;

    public InvalidTokenRequestException(final String tokenType, final String token, final String message) {
        super(String.format("%s: [%s] token: [%s] ", message, tokenType, token));
        this.tokenType = tokenType;
        this.token = token;
        this.message = message;
    }
}
