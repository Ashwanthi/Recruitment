package com.qbrainx_recruitment.service;

import java.util.List;

import javax.validation.Valid;

import com.qbrainx_recruitment.dto.UserRoleMapDto;

public interface UserRoleMapService {

	UserRoleMapDto save(@Valid UserRoleMapDto userRoleMapDto);

	// List<UserRoleMapDto> get();

}
