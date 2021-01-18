package com.qbrainx_recruitment.dto;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class CustomUserInfo implements Serializable {

    private static final long serialVersionUID = 2327519646222077323L;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private Long mobileNo;

    private String gender;

    private String nationality;
}
