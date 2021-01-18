package com.qbrainx_recruitment.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qbrainx_recruitment.dto.PermissionDto;
import com.qbrainx_recruitment.model.Permission;
import com.qbrainx_recruitment.repository.PermissionRepository;
import com.qbrainx_recruitment.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public List<PermissionDto> getPermission() {
		// TODO Auto-generated method stub
		List<PermissionDto> dto = permissionRepository.findAll().stream().map(permission -> {
			PermissionDto permissionDto = new PermissionDto();
			BeanUtils.copyProperties(permission, permissionDto);
			return permissionDto;
		}).collect(Collectors.toList());
		return dto;
	}

	@Override
	public PermissionDto save(@Valid PermissionDto permissiondto) {
		// TODO Auto-generated method stub
		ModelMapper modelmapper = new ModelMapper();
		Permission permission = modelmapper.map(permissiondto, Permission.class);
		permission = permissionRepository.save(permission);
		return permissiondto;
	}

}
