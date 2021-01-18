package com.qbrainx_recruitment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermissionMapDto extends AuditModelDto {

	private static final long serialVersionUID = 7595549326007282571L;

	private Long roleId;

	private Long permissionId;

	private PermissionDto permission;

}
