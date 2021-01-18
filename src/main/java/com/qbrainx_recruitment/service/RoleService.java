package com.qbrainx_recruitment.service;

import java.util.List;

import javax.validation.Valid;

import com.qbrainx_recruitment.dto.RoleDto;

public interface RoleService {

	List<RoleDto> getRoles();

	RoleDto save(@Valid RoleDto roledto);

}
