package com.qbrainx_recruitment.serviceImpl;

import com.qbrainx_recruitment.dto.UserRoleMapDto;
import com.qbrainx_recruitment.exception.ResourceNotFoundException;
import com.qbrainx_recruitment.model.UserRoleMap;
import com.qbrainx_recruitment.repository.RoleRepository;
import com.qbrainx_recruitment.repository.UserRepository;
import com.qbrainx_recruitment.repository.UserRoleMapRepository;
import com.qbrainx_recruitment.service.UserRoleMapService;
import com.qbrainx_recruitment.util.CustomConverters;
import com.qbrainx_recruitment.util.ModelConverters;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class UserRoleMapServiceImpl implements UserRoleMapService {

	private final UserRoleMapRepository userRoleMapRepository;

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final ModelConverters<UserRoleMapDto, UserRoleMap> modelConverters;

	public UserRoleMapServiceImpl(final UserRoleMapRepository userRoleMapRepository,
								  final UserRepository userRepository,
								  final RoleRepository roleRepository,
								  final DozerBeanMapper dozerBeanMapper) {
		this.userRoleMapRepository = userRoleMapRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		modelConverters = CustomConverters.converter(dozerBeanMapper, UserRoleMapDto.class, UserRoleMap.class);
	}

	@Override	
	public UserRoleMapDto save(@Valid final UserRoleMapDto userRoleMapDto) {
		userRepository.findById(userRoleMapDto.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("userId", "UserId not found"));
		roleRepository.findById(userRoleMapDto.getRoleId())
				.orElseThrow(() -> new ResourceNotFoundException("userId", "UserId not found"));
		final List<UserRoleMap> foundRole = userRoleMapRepository.findByUserId(userRoleMapDto.getUserId());
		if(foundRole.isEmpty()) {
			final UserRoleMap userRoleMap = modelConverters.convertVoToEntity(userRoleMapDto);
			return modelConverters.convertEntityToVo(userRoleMapRepository.save(userRoleMap));
		} else {
				throw new RuntimeException("user already mapped");
		}
	}

}
