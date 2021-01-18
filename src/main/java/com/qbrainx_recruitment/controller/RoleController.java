package com.qbrainx_recruitment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qbrainx_recruitment.dto.RoleDto;
import com.qbrainx_recruitment.service.RoleService;

@RestController
@RequestMapping("/v1/roles")
public class RoleController {
	@Autowired
	private RoleService roleService;

	@GetMapping
	public List<RoleDto> getAllRoles() {
		return roleService.getRoles();
	}

	@PostMapping
	public RoleDto createUser(@Valid @RequestBody final RoleDto roledto) {
		return roleService.save(roledto);
	}

}
