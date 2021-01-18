package com.qbrainx_recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qbrainx_recruitment.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
