package com.qbrainx_recruitment.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "APPLICANT")
@Getter
@Setter
public class Applicant extends AuditModel {

	private static final long serialVersionUID = 4927214596374767331L;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email_id", nullable = false)
	private String emailId;

	@Column(name = "age")
	private Long age;

	@Column(name = "years_of_exp")
	private String yearsOfExp;

	@Column(name = "education_info")
	private String educationInfo;

	@Column(name = "is_spouse_present")
	private Boolean isSpousePresent;
	
	@Column(name = "assessment_type")
	private String assessmentType;


}
