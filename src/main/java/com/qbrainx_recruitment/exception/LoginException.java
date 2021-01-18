package com.qbrainx_recruitment.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class LoginException extends RuntimeException {

    private static final long serialVersionUID = -4514924110216245121L;

    public LoginException(final String message) {
        super(message);
    }

    public LoginException(final String message,final  Throwable cause) {
        super(message, cause);
    }
}
