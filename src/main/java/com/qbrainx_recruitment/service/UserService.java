package com.qbrainx_recruitment.service;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qbrainx_recruitment.dto.UserInfoDto;
import com.qbrainx_recruitment.dto.UsersDto;
import com.qbrainx_recruitment.model.Users;

public interface UserService {

	Page<UsersDto> getUsers(Pageable page, Integer pageNo, Integer pageSize, String sortBy, String sortOrder);

	UsersDto save(@Valid UsersDto userdto);

	UserInfoDto userByEmailId(String emailId);

	Optional<Users> findByEmail(String email);

	UsersDto userFindById(Long id);

	Optional<Users> findById(Long id);

	UsersDto update(UsersDto usersdto);

	Map<String, Boolean> deleteAll();

	Map<String, Boolean> delete(Long id);

    Boolean existsByEmail(String email);

    Users createUser(UsersDto usersDto);

	void logoutUser(Long id);
	
	Users saveUsers(Users users);
}
