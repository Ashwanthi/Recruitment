package com.qbrainx_recruitment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRoleMapDto extends AuditModelDto {

	private static final long serialVersionUID = 7595549326007282571L;

	private Long roleId;

	private Long userId;

}
