package com.qbrainx_recruitment.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qbrainx_recruitment.dto.PermissionDto;
import com.qbrainx_recruitment.dto.RolePermissionMapDto;
import com.qbrainx_recruitment.model.Permission;
import com.qbrainx_recruitment.model.Role;
import com.qbrainx_recruitment.model.RolePermissionMap;
import com.qbrainx_recruitment.repository.PermissionRepository;
import com.qbrainx_recruitment.repository.RolePermissionMapRepository;
import com.qbrainx_recruitment.repository.RoleRepository;
import com.qbrainx_recruitment.service.RolePermissionMapService;

@Service
public class RolePermissionMapServiceImpl implements RolePermissionMapService {
	@Autowired
	private RolePermissionMapRepository rolepermissionmapRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public List<RolePermissionMapDto> get() {
		// TODO Auto-generated method stub
		List<RolePermissionMapDto> dto = rolepermissionmapRepository.findAll().stream().map(rolepermissionmap -> {
			RolePermissionMapDto rolepermissionmapDto = new RolePermissionMapDto();
			BeanUtils.copyProperties(rolepermissionmap, rolepermissionmapDto);
			System.out.println("Permission list:");
			System.out.println(rolepermissionmap.getPermission().getPermissionName());
			PermissionDto pDto = new PermissionDto();
			BeanUtils.copyProperties(rolepermissionmap.getPermission(), pDto);
			rolepermissionmapDto.setPermission(pDto);
			return rolepermissionmapDto;
		}).collect(Collectors.toList());

		return dto;
	}

	@Override
	public RolePermissionMapDto save(@Valid RolePermissionMapDto rolepermissionmapDto) {
		// TODO Auto-generated method stub
		Optional<Role> rolefoundId = roleRepository.findById(rolepermissionmapDto.getRoleId());
		Optional<Permission> permissionfoundId = permissionRepository.findById(rolepermissionmapDto.getPermissionId());
		if (rolefoundId.isPresent() && permissionfoundId.isPresent()) {
			RolePermissionMap rpm = new RolePermissionMap();
			BeanUtils.copyProperties(rolepermissionmapDto, rpm);
			rolepermissionmapRepository.save(rpm);
			return rolepermissionmapDto;
		} else {
			throw new RuntimeException("Either PermissionId or RoleID not found");
		}
	}

}
