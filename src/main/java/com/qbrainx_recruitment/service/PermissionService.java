package com.qbrainx_recruitment.service;

import java.util.List;

import javax.validation.Valid;

import com.qbrainx_recruitment.dto.PermissionDto;

public interface PermissionService {

	List<PermissionDto> getPermission();

	PermissionDto save(@Valid PermissionDto permissiondto);

}
