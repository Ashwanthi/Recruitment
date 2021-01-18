package com.qbrainx_recruitment.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse implements Serializable {

    private static final long serialVersionUID = 9105209363630919928L;

    private final String data;
    private final Boolean success;
    private final String timestamp;
    private final String cause;
    private final String path;
}

