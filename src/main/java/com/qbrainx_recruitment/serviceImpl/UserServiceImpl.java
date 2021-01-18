package com.qbrainx_recruitment.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.qbrainx_recruitment.dto.CustomUserInfo;
import com.qbrainx_recruitment.dto.PermissionDto;
import com.qbrainx_recruitment.dto.RoleDto;
import com.qbrainx_recruitment.dto.UserInfoDto;
import com.qbrainx_recruitment.dto.UsersDto;
import com.qbrainx_recruitment.exception.LoginException;
import com.qbrainx_recruitment.model.RolePermissionMap;
import com.qbrainx_recruitment.model.UserRoleMap;
import com.qbrainx_recruitment.model.UserTokenStatus;
import com.qbrainx_recruitment.model.Users;
import com.qbrainx_recruitment.repository.RolePermissionMapRepository;
import com.qbrainx_recruitment.repository.UserRepository;
import com.qbrainx_recruitment.repository.UserRoleMapRepository;
import com.qbrainx_recruitment.service.RefreshTokenService;
import com.qbrainx_recruitment.service.UserService;
import com.qbrainx_recruitment.service.UserTokenStatusService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleMapRepository userrolemapRepository;

	@Autowired
	private RolePermissionMapRepository rolePermissionMapRepository;

	private final UserTokenStatusService userTokenStatusService;

	private final RefreshTokenService refreshTokenService;

	public UserServiceImpl(final UserTokenStatusService userTokenStatusService,
			final RefreshTokenService refreshTokenService) {
		this.userTokenStatusService = userTokenStatusService;
		this.refreshTokenService = refreshTokenService;
	}

	@Override
	public Page<UsersDto> getUsers(Pageable page, Integer pageNo, Integer pageSize, String sortby, String sortOrder) {
		
		if (sortOrder.equalsIgnoreCase("Asc")) {
			page = PageRequest.of(pageNo, pageSize, Sort.by(sortby).ascending());
		} else {
			page = PageRequest.of(pageNo, pageSize, Sort.by(sortby).descending());
		}
		return userRepository.findAll(page).map(users -> {
			UsersDto uDto = new UsersDto();
			BeanUtils.copyProperties(users, uDto);
			return uDto;
		});
	}

	@Override
	public UsersDto save(@Valid UsersDto userdto) {
		final ModelMapper modelmapper = new ModelMapper();
		Users user = modelmapper.map(userdto, Users.class);
		final int mobileCount = 10;
		if (user.getMobileNo().toString().length() != mobileCount) {
			throw new RuntimeException("Mobile Number should be 10 digits only.");
		}
		String passwordEncrypt = passwordEncoder.encode(user.getPassword());
		user.setPassword(passwordEncrypt);
		user.setActive(Boolean.TRUE);
		user = userRepository.save(user);
		modelmapper.map(user, userdto);
		return userdto;
	}

	@Override
	public UserInfoDto userByEmailId(String emailId) {
		final Users userfoundEmail = userRepository.findByEmail(emailId)
				.orElseThrow(() -> new RuntimeException("emailId not found"));
		UserInfoDto userInfoDto = new UserInfoDto();
		CustomUserInfo customUserInfo = new CustomUserInfo();
		RoleDto roleDtoTemp = new RoleDto();
		PermissionDto rpDto = new PermissionDto();
		BeanUtils.copyProperties(userfoundEmail, customUserInfo);
		userInfoDto.setUser(customUserInfo);

		final List<UserRoleMap> useRoleMapList = userrolemapRepository.findByUserId(userfoundEmail.getId());

		List<Long> roleId = useRoleMapList.stream().map(UserRoleMap::getRoleId).collect(Collectors.toList());

		if (!useRoleMapList.isEmpty()) {
			useRoleMapList.forEach(roleMap -> {
				BeanUtils.copyProperties(roleMap.getRole(), roleDtoTemp);
				userInfoDto.setRoleName(roleDtoTemp.getRoleName());
			});
		} else {
			throw new RuntimeException("User is not assigned to any role.");
		}
		List<RolePermissionMap> rolepermission = rolePermissionMapRepository.findByRoleIdIn(roleId);
		List<String> ex = new ArrayList<String>();
		rolepermission.forEach(permission -> {
			BeanUtils.copyProperties(permission.getPermission(), rpDto);
			ex.add(rpDto.getPermissionName());
		});
		userInfoDto.setPermissionName(ex);
		return userInfoDto;
	}

	@Override
	public Optional<Users> findByEmail(final String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UsersDto userFindById(Long userid) {
		// TODO Auto-generated method stub
		Optional<Users> userfoundId = userRepository.findById(userid);
		if (userfoundId.isPresent()) {
			Users user = userRepository.findById(userid).get();
			UsersDto dto = new UsersDto();
			ModelMapper modelmapper = new ModelMapper();
			modelmapper.map(user, dto);
			return dto;
		} else {
			throw new RuntimeException("Id not found");
		}
	}

	@Override
	public Optional<Users> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public UsersDto update(UsersDto userdto) {
		Optional<Users> userfoundId = userRepository.findById(userdto.getId());
		if (userfoundId.isPresent()) {
			ModelMapper modelmapper = new ModelMapper();
			Users user = modelmapper.map(userdto, Users.class);
			int mobileCount = 10;
			if (user.getMobileNo().toString().length() != mobileCount) {
				throw new RuntimeException("Mobile Number should be 10 digits only.");
			}
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user = userRepository.save(user);
			return userdto;
		} else {
			throw new RuntimeException("Id not found");
		}
	}

	@Override
	public Map<String, Boolean> deleteAll() {
		// TODO Auto-generated method stub
		Map<String, Boolean> response = new HashMap<>();
		userRepository.deleteAll();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@Override
	public Map<String, Boolean> delete(Long userid) {
		// TODO Auto-generated method stub
		Map<String, Boolean> response = new HashMap<>();
		Optional<Users> user = userRepository.findById(userid);
		if (user.isPresent()) {
			userRepository.deleteById(userid);
			response.put("deleted", Boolean.TRUE);
		} else {
			response.put("not success - UserId not found", Boolean.FALSE);
		}
		return response;
	}

	private PermissionDto buildPermissions(UserInfoDto userInfoDto, PermissionDto rpDto, List<Long> roleId) {
		List<RolePermissionMap> rolepermission = rolePermissionMapRepository.findByRoleIdIn(roleId);
		List<String> ex = new ArrayList<String>();
		rolepermission.forEach(permission -> {
			BeanUtils.copyProperties(permission.getPermission(), rpDto);
			ex.add(rpDto.getPermissionName());
		});
		userInfoDto.setPermissionName(ex);

		return rpDto;
	}

	private CustomUserInfo buildUserInfo(String emailId, CustomUserInfo customUserInfo, UserInfoDto userInfoDto) {
		Users userfoundEmail = userRepository.findByEmail(emailId)
				.orElseThrow(() -> new RuntimeException("emailId not found"));
		BeanUtils.copyProperties(userfoundEmail, customUserInfo);
		userInfoDto.setUser(customUserInfo);
		return customUserInfo;
	}

	private List<Long> buildRoles(Long id, RoleDto roleDtoTemp, UserInfoDto userInfoDto) {
		List<UserRoleMap> useRoleMapList = userrolemapRepository.findByUserId(id);
		List<Long> roleId = useRoleMapList.stream().map(UserRoleMap::getRoleId).collect(Collectors.toList());
		if (!useRoleMapList.isEmpty()) {
			useRoleMapList.forEach(roleMap -> {
//				roleMap.getRole().forEach(role -> {
//					BeanUtils.copyProperties(role, roleDtoTemp);
//					userInfoDto.setRoleName(roleDtoTemp.getRoleName());
//				});
			});

		} else {
			throw new RuntimeException("User is not assigned to any role.");
		}
		return roleId;
	}

	@Override
	public Boolean existsByEmail(final String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public Users createUser(final UsersDto usersDto) {
		// @Todo: Create the usersDto
		return null;
	}

	@Override
	public void logoutUser(final Long id) {
		final UserTokenStatus userTokenStatus = userTokenStatusService.findByUserId(id)
				.orElseThrow(() -> new LoginException("user logged out already"));
		logger.info("Removing refresh token associated with user [" + id + "]");
		refreshTokenService.deleteById(userTokenStatus.getRefreshToken().getId());
	}

	@Override
	public Users saveUsers(Users users) {
		return userRepository.save(users);
	}

}
