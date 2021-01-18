package com.qbrainx_recruitment.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ROLE_MAP")
@Getter
@Setter
public class UserRoleMap extends AuditModel {

	private static final long serialVersionUID = 7595549326007282571L;

	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "ROLE_ID")
	private Long roleId;

	@OneToOne(targetEntity = Users.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "USER_ID", insertable = false, updatable = false,
			foreignKey =  @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
	private Users users;

	@OneToOne(targetEntity = Role.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "ROLE_ID", insertable = false, updatable = false,
			foreignKey =  @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
	private Role role;
}
