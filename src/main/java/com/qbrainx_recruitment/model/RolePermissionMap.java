package com.qbrainx_recruitment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "ROLE_PERMISSION_MAP")
@Getter
@Setter
public class RolePermissionMap extends AuditModel {

	private static final long serialVersionUID = 7595549326007282571L;

	@Column(name = "ROLE_ID")
	private Long roleId;

	@Column(name = "PERMISSION_ID")
	private Long permissionId;

	@ManyToOne(targetEntity = Role.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "ROLE_ID", insertable = false, updatable = false)
	private Role role;

	@ManyToOne(targetEntity = Permission.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "PERMISSION_ID", insertable = false, updatable = false)
	private Permission permission;

}
