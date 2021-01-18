package com.qbrainx_recruitment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class LoginRequestDto implements Serializable {

    private static final long serialVersionUID = -3098252329838246481L;

    @ApiModelProperty(value = "User registered email", required = true, allowableValues = "NonEmpty String")
    @NotNull(message = "Login password cannot be blank")
    private String email;

    @ApiModelProperty(value = "Valid user password", required = true, allowableValues = "NonEmpty String")
    @NotNull(message = "Login password cannot be blank")
    private String password;
}
