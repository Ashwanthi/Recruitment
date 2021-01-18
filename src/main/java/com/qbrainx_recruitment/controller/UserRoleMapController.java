package com.qbrainx_recruitment.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qbrainx_recruitment.dto.UserRoleMapDto;
import com.qbrainx_recruitment.service.UserRoleMapService;

@RestController
@RequestMapping("/v1/user-role")
public class UserRoleMapController {

	@Autowired
	private UserRoleMapService userRoleMapService;

	@PostMapping
	public UserRoleMapDto createUser(@Valid @RequestBody final UserRoleMapDto userRoleMapDto) {
		return userRoleMapService.save(userRoleMapDto);
	}

}
