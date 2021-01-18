package com.qbrainx_recruitment.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ROLE")
@Getter
@Setter
public class Role extends AuditModel {

	private static final long serialVersionUID = -3692803105970529827L;

	@Column(name = "ROLE_NAME")
	private String roleName;

	@Column(name = "ROLE_DESCRIPTION")
	private String roleDescription;
}
