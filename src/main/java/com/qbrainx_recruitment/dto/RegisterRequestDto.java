package com.qbrainx_recruitment.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RegisterRequestDto implements Serializable {

    private static final long serialVersionUID = 9026747524628316139L;

    private String email;

    private String password;
}
