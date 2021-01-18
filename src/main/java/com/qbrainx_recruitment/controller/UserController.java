package com.qbrainx_recruitment.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qbrainx_recruitment.dto.UserInfoDto;
import com.qbrainx_recruitment.dto.UsersDto;
import com.qbrainx_recruitment.event.UserLogoutSuccessEvent;
import com.qbrainx_recruitment.security.ISecurityContext;
import com.qbrainx_recruitment.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	private final ISecurityContext securityContext;

	private final ApplicationEventPublisher applicationEventPublisher;

	public UserController(final UserService userService,
						  final ISecurityContext securityContext,
						  final ApplicationEventPublisher applicationEventPublisher) {
		this.userService = userService;
		this.securityContext = securityContext;
		this.applicationEventPublisher = applicationEventPublisher;
	}


	@GetMapping
	public Page<UsersDto> getAllUsers(Pageable page, @RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "Asc") String sortOrder) {
		return userService.getUsers(page, pageNo, pageSize, sortBy, sortOrder);
	}

	@GetMapping("/email/{emailId}")
	public ResponseEntity<UserInfoDto> getUserByEmail(@PathVariable("emailId") final String emailId) {
		UserInfoDto user = userService.userByEmailId(emailId);
		return ResponseEntity.ok().body(user);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsersDto> getUserById(@PathVariable("id") final Long id) {
		UsersDto user = userService.userFindById(id);
		return ResponseEntity.ok().body(user);
	}

	@PostMapping
	public UsersDto createUser(@Valid @RequestBody UsersDto userdto) {
		return userService.save(userdto);
	}

	@PutMapping
	public UsersDto updateUser(@RequestBody UsersDto usersdto) {
		return userService.update(usersdto);
	}

	@DeleteMapping("/id/{id}")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long id) {
		return userService.delete(id);
	}
	
	/*@GetMapping("/userIfo/{emailId}")
	public ResponseEntity<UserInfoDto> getUserInfoEmail(@PathVariable("emailId") final String emailId) {
		UserInfoDto user = userService.userInfoByEmailId(emailId);
		return ResponseEntity.ok().body(user);
	}
*/
	@PostMapping("/logout")
	@ApiOperation(value = "Logs the specified user device and clears the refresh tokens associated with it")
	public ResponseEntity logoutUser() {
		userService.logoutUser(securityContext.getPrincipal().getId());
		final Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
		final UserLogoutSuccessEvent userLogoutSuccessEvent = new UserLogoutSuccessEvent(securityContext.getPrincipal().getName(),
				credentials.toString());
		applicationEventPublisher.publishEvent(userLogoutSuccessEvent);
		return ResponseEntity.ok("Log out successful");
	}
}
