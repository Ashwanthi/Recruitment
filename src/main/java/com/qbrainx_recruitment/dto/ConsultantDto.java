package com.qbrainx_recruitment.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultantDto extends AuditModelDto {

	private static final long serialVersionUID = 1338047617465455048L;

	private UsersDto user;

	private String roleName;

	private String ConsultantId;

	private String Address;

	private String paymentMode;

	private String SubscriptionType;

	private Date subscriptionEndDate;

	private Date enrollmentDate;

	private Long licenseNumber;

	private Date licenseValidityFrom;

	private Date licenseValidityTo;

	private String billingRate;

	private String hoursWorked;

	private Long couponsAvailed;

	private Long licensingOrganization;

	private String Comment;

	private String bankAccount;

	private String Commission;

	private String userName;

}
