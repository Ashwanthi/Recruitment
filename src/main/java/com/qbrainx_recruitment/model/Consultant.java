package com.qbrainx_recruitment.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Consultant")
@Getter
@Setter
public class Consultant extends AuditModel {

	private static final long serialVersionUID = -3692803105970529827L;

	@Column(name = "role")
	private String roleName;

	@Column(name = "consultantId")
	private String ConsultantId;

	@Column(name = "address")
	private String Address;

	@Column(name = "paymentMode")
	private String paymentMode;

	@Column(name = "subscription_type")
	private String SubscriptionType;

	@Column(name = "subscription_end_date")
	private Date subscriptionEndDate;

	@Column(name = "enrollmentDate")
	private Date enrollmentDate;

	@Column(name = "licenseNumber")
	private Long licenseNumber;

	@Column(name = "licenseValidityFrom")
	private Date licenseValidityFrom;

	@Column(name = "licenseValidityTo")
	private Date licenseValidityTo;

	@Column(name = "billingRate")
	private String billingRate;

	@Column(name = "hoursWorked")
	private String hoursWorked;

	@Column(name = "couponsAvailed")
	private Long couponsAvailed;

	@Column(name = "licensingOrganization")
	private Long licensingOrganization;

	@Column(name = "comment")
	private String Comment;

	@Column(name = "bankAccount")
	private String bankAccount;

	@Column(name = "Commission")
	private String Commission;

	@Column(name = "userName")
	private String userName;

}
