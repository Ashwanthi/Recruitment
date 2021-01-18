package com.qbrainx_recruitment.serviceImpl;

import javax.validation.Valid;

import org.dozer.DozerBeanMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.qbrainx_recruitment.controller.AuthorizationController;
import com.qbrainx_recruitment.dto.ConsultantDto;
import com.qbrainx_recruitment.dto.UsersDto;
import com.qbrainx_recruitment.exception.ResourceNotFoundException;
import com.qbrainx_recruitment.model.Consultant;
import com.qbrainx_recruitment.model.Role;
import com.qbrainx_recruitment.model.UserRoleMap;
import com.qbrainx_recruitment.repository.ConsultantRepository;
import com.qbrainx_recruitment.repository.RoleRepository;
import com.qbrainx_recruitment.repository.UserRepository;
import com.qbrainx_recruitment.repository.UserRoleMapRepository;
import com.qbrainx_recruitment.security.JwtTokenProvider;
import com.qbrainx_recruitment.service.AuthService;
import com.qbrainx_recruitment.service.ConsultantService;
import com.qbrainx_recruitment.util.CustomConverters;
import com.qbrainx_recruitment.util.ModelConverters;

@Service
public class ConsultantServiceImpl implements ConsultantService {

	private final ConsultantRepository consultantRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	private final ModelConverters<ConsultantDto, Consultant> modelConverters;

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	private UserRoleMapRepository userrolemapRepository;

	private final AuthService authService;
	private final JwtTokenProvider tokenProvider;
	private final ApplicationEventPublisher applicationEventPublisher;

	public ConsultantServiceImpl(final ConsultantRepository consultantRepository, final UserRepository userRepository,
			final RoleRepository roleRepository, final UserRoleMapRepository userrolemapRepository,
			final DozerBeanMapper dozerBeanMapper, final AuthService authService, JwtTokenProvider tokenProvider,
			ApplicationEventPublisher applicationEventPublisher) {
		this.consultantRepository = consultantRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userrolemapRepository = userrolemapRepository;
		this.authService = authService;
		this.tokenProvider = tokenProvider;
		this.applicationEventPublisher = applicationEventPublisher;
		modelConverters = CustomConverters.converter(dozerBeanMapper, ConsultantDto.class, Consultant.class);
	}

	@Override
	public ConsultantDto save(@Valid ConsultantDto consultantDto) {
		ModelMapper modelmapper = new ModelMapper();
		Consultant consultant = modelmapper.map(consultantDto, Consultant.class);
		Role role = roleRepository.findByRoleName(consultant.getRoleName())
				.orElseThrow(() -> new ResourceNotFoundException("roleName", "Role not found"));
		UsersDto usersDto = consultantDto.getUser();
		AuthorizationController authorizationController = new AuthorizationController(authService, tokenProvider,
				applicationEventPublisher);
		usersDto.setPassword("cannext@123");
		authorizationController.registerUser(usersDto);
		userRepository.findById(usersDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("userId", "UserId not found"));
		roleRepository.findById(role.getId())
				.orElseThrow(() -> new ResourceNotFoundException("roleId", "RoleId not found"));
		UserRoleMap userroleMap = new UserRoleMap();
		userroleMap.setUserId(usersDto.getId());
		userroleMap.setRoleId(role.getId());
		userrolemapRepository.save(userroleMap);
		return consultantDto;
	}

}
