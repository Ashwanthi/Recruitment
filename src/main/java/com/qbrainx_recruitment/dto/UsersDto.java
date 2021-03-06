package com.qbrainx_recruitment.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersDto extends AuditModelDto {

	private static final long serialVersionUID = -5051044480556257171L;

	@NotEmpty(message = "Name may not be empty")
	@NotBlank(message = "Name should not be empty")
	@NotNull
	private String firstName;

	private String middleName;

	@NotEmpty(message = "Name may not be empty")
	@NotBlank(message = "Name should not be empty")
	@NotNull
	private String lastName;

	@NotBlank(message = "Please provide email")
	@Email(message = "Please provide a valid email")
	private String email;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private Long mobileNo;

	private String gender;

	private String nationality;

	private Boolean active;

	private Boolean isEmailVerified;
}
