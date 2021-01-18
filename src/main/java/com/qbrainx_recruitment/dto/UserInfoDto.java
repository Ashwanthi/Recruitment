package com.qbrainx_recruitment.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserInfoDto implements Serializable {

	private static final long serialVersionUID = -2861303024041586007L;

	private CustomUserInfo user;

	private String roleName;

	private List<String> permissionName;

}
