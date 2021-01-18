package com.qbrainx_recruitment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qbrainx_recruitment.dto.PermissionDto;
import com.qbrainx_recruitment.service.PermissionService;

@RequestMapping("/v1/permission")
@RestController
public class PermissionController {
	@Autowired
	private PermissionService permissionService;

	@GetMapping
	public List<PermissionDto> getAllPermission() {
		return permissionService.getPermission();
	}

	@PostMapping
	public PermissionDto createUser(@Valid @RequestBody PermissionDto permissiondto) {
		return permissionService.save(permissiondto);
	}

}
