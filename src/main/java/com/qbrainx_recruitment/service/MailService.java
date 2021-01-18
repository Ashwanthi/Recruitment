package com.qbrainx_recruitment.service;

import javax.mail.MessagingException;

import com.qbrainx_recruitment.dto.ApplicantDto;
import com.qbrainx_recruitment.dto.Mail;

public interface MailService {

    void sendEmailVerification(String emailVerificationUrl, String toAddress);

    void sendResetLink(String resetPasswordLink, String toAddress);

    void sendAccountChangeEmail(String action, String actionStatus, String toAddress);

    void send(Mail mail) throws MessagingException;

	void sendApplicantFormProcessEmail(ApplicantDto applicant);
}
