package com.qbrainx_recruitment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RoleDto extends AuditModelDto {

	private static final long serialVersionUID = 1338047617465455048L;

	@NotNull(message = "Role Name Cannot be Empty")
	private String roleName;

	@NotNull(message = "Role Description Cannot be Empty")
	private String roleDescription;
}
