package com.qbrainx_recruitment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PermissionDto extends AuditModelDto {

	private static final long serialVersionUID = -9041387754369941381L;

	private String permissionName;

	private Boolean isAdminpermission;

	private List<RolePermissionMapDto> rolepermissionmap;


}
