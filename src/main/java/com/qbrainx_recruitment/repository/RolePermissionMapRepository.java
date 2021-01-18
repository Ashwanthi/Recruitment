package com.qbrainx_recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qbrainx_recruitment.model.RolePermissionMap;

@Repository
public interface RolePermissionMapRepository extends JpaRepository<RolePermissionMap, Long> {

	List<RolePermissionMap> findByRoleId(Long long1);

	List<RolePermissionMap> findByRoleIdIn(List<Long> roleId);

}
