package com.qbrainx_recruitment.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicantDto extends AuditModelDto {

	private static final long serialVersionUID = -6609314561909989078L;

	@NotEmpty(message = "firstName should not be empty")
	@NotBlank(message = "firstName should not be empty")
	@NotNull
	private String firstName;

	@NotEmpty(message = "lastName should not be empty")
	@NotBlank(message = "lastName should not be empty")
	@NotNull
	private String lastName;

	@NotBlank(message = "Please provide email")
	@Email(message = "Please provide a valid email")
	private String emailId;

	private Long age;

	private String yearsOfExp;

	private String educationInfo;

	private Boolean isSpousePresent;

	@NotEmpty(message = "assessmentType should not be empty")
	@NotBlank(message = "assessmentType should not be empty")
	@NotNull
	private String assessmentType;

	public ApplicantDto(final String firstName, final String lastName, final String emailId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
	}
}