package com.qbrainx_recruitment.service;

import java.util.List;

import javax.validation.Valid;

import com.qbrainx_recruitment.dto.RolePermissionMapDto;

public interface RolePermissionMapService {

	RolePermissionMapDto save(@Valid RolePermissionMapDto rolepermissionmapDto);

	List<RolePermissionMapDto> get();

}
