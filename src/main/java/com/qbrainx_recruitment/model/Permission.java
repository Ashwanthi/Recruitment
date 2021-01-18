package com.qbrainx_recruitment.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "PERMISSION")
@Getter
@Setter
public class Permission extends AuditModel {

	private static final long serialVersionUID = -6369525047572797358L;

	@Column(name = "PERMISSION_NAME")
	private String permissionName;

	@Column(name = "IS_ADMIN")
	private Boolean isAdmin;

	@JsonBackReference
	@OneToMany(mappedBy = "permission", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@Fetch(FetchMode.JOIN)
	private List<RolePermissionMap> rolePermissionMaps;

}
