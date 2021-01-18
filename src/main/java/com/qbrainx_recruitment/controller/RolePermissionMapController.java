package com.qbrainx_recruitment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qbrainx_recruitment.dto.RolePermissionMapDto;
import com.qbrainx_recruitment.service.RolePermissionMapService;

@RestController
@RequestMapping("/v1/role-permission")
public class RolePermissionMapController {
	@Autowired
	private RolePermissionMapService rolepermissionmapService;

	@GetMapping
	public List<RolePermissionMapDto> getAll() {
		return rolepermissionmapService.get();
	}

	@PostMapping
	public RolePermissionMapDto createUser(@Valid @RequestBody RolePermissionMapDto rolepermissionmapDto) {
		return rolepermissionmapService.save(rolepermissionmapDto);
	}

}
