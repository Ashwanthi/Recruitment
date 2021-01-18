package com.qbrainx_recruitment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6943192520056520505L;

    private final String resourceName;
    private final String fieldName;


    public ResourceNotFoundException(final String resourceName, final String fieldName) {
        this.resourceName = resourceName;
        this.fieldName = fieldName;
    }
}
