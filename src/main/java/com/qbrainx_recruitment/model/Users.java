package com.qbrainx_recruitment.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "USERS", uniqueConstraints = { @UniqueConstraint(columnNames = { "EMAIL"}) })
@Getter
@Setter
public class Users extends AuditModel {

	private static final long serialVersionUID = -4048842797313767190L;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "MOBILE_NO")
	private Long mobileNo;

	@Column(name = "GENDER")
	private String gender;

	@Column(name = "NATIONALITY")
	private String nationality;

	@Column(name = "IS_ACTIVE", nullable = false)
	private Boolean active;

	@Column(name = "IS_EMAIL_VERIFIED", nullable = false)
	private Boolean isEmailVerified;

	@OneToOne(optional = true, mappedBy = "users", cascade = CascadeType.ALL)
	private UserRoleMap role;

	public String getRoleFromChild() {
		if (this.role.getRole() != null ) {
			return this.role.getRole().getRoleName() != null ? this.role.getRole().getRoleName() : "";
		} else {
			return "";
		}
	}
}
