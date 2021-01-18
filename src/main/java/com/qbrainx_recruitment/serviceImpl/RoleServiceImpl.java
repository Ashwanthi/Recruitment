package com.qbrainx_recruitment.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.qbrainx_recruitment.util.CustomConverters;
import com.qbrainx_recruitment.util.ModelConverters;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import com.qbrainx_recruitment.dto.RoleDto;
import com.qbrainx_recruitment.model.Role;
import com.qbrainx_recruitment.repository.RoleRepository;
import com.qbrainx_recruitment.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	private final ModelConverters<RoleDto, Role> modelConverters;

	public RoleServiceImpl(final RoleRepository roleRepository, final DozerBeanMapper dozerBeanMapper) {
		this.roleRepository = roleRepository;
		modelConverters = CustomConverters.converter(dozerBeanMapper, RoleDto.class, Role.class);
	}

	@Override
	public List<RoleDto> getRoles() {
		return modelConverters.convertEntityToVo(roleRepository.findAll());
	}

	@Override
	public RoleDto save(@Valid final RoleDto roleDto) {
		final Role role = modelConverters.convertVoToEntity(roleDto);
		return modelConverters.convertEntityToVo(roleRepository.save(role));
	}

}
