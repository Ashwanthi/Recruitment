package com.qbrainx_recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qbrainx_recruitment.model.UserRoleMap;

@Repository
public interface UserRoleMapRepository extends JpaRepository<UserRoleMap, Long> {

	List<UserRoleMap> findByUserId(Long id);

}
