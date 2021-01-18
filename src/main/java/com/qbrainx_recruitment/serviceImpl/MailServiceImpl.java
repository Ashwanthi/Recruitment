package com.qbrainx_recruitment.serviceImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.qbrainx_recruitment.dto.ApplicantDto;
import com.qbrainx_recruitment.dto.Mail;
import com.qbrainx_recruitment.service.MailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger logger = Logger.getLogger(MailServiceImpl.class);

    @Value("${app.velocity.templates.location}")
    private String basePackagePath;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${app.token.password.reset.duration}")
    private Long expiration;

    private final JavaMailSender javaMailSender;

    private final Configuration configuration;

    public MailServiceImpl(final JavaMailSender mailSender, final Configuration templateConfiguration) {
        this.javaMailSender = mailSender;
        this.configuration = templateConfiguration;
    }

    @Override
    public void sendEmailVerification(final String emailVerificationUrl, final String toAddress) {
        final Mail mail = new Mail();
        mail.setSubject("Email Verification - CanNext");
        mail.setToAddress(toAddress);
        mail.setFromAddress(mailFrom);
        mail.getModel().put("userName", toAddress);
        mail.getModel().put("userEmailTokenVerificationLink", emailVerificationUrl);
        try {
            configuration.setClassForTemplateLoading(getClass(), basePackagePath);
            final Template template = configuration.getTemplate("verifyEmail.ftl");
            final String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
            mail.setContent(mailContent);
            send(mail);
        } catch (final IOException | TemplateException | MessagingException exception) {
            logger.debug("Mail Send Failure due to " + exception.getMessage());
        }

    }

    @Override
    public void sendResetLink(final String resetPasswordLink, final String toAddress) {
        final Mail mail = new Mail();
        final Long expirationInMinutes = TimeUnit.MILLISECONDS.toMinutes(expiration);
        final String expirationInMinutesString = expirationInMinutes.toString();
        mail.setSubject("Password Rest Link - CanNext");
        mail.setToAddress(toAddress);
        mail.setFromAddress(mailFrom);
        mail.getModel().put("userName", toAddress);
        mail.getModel().put("userResetPasswordLink", resetPasswordLink);
        mail.getModel().put("expirationTime", expirationInMinutesString);
        try {
            configuration.setClassForTemplateLoading(getClass(), basePackagePath);
            final Template template = configuration.getTemplate("resetLink.ftl");
            final String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
            mail.setContent(mailContent);
            send(mail);
        } catch (final IOException | TemplateException | MessagingException exception) {
            logger.debug("Mail Send Failure due to " + exception.getMessage());
        }
    }

    @Override
    public void sendAccountChangeEmail(final String action, final String actionStatus, final String toAddress) {
        final Mail mail = new Mail();
        mail.setSubject("Account Status Change [Team CEP]");
        mail.setToAddress(toAddress);
        mail.setFromAddress(mailFrom);
        mail.getModel().put("userName", toAddress);
        mail.getModel().put("action", action);
        mail.getModel().put("actionStatus", actionStatus);
        try {
            configuration.setClassForTemplateLoading(getClass(), basePackagePath);
            final Template template = configuration.getTemplate("accountChange.ftl");
            final String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
            mail.setContent(mailContent);
            send(mail);
        } catch (final IOException | TemplateException | MessagingException exception) {
            logger.debug("Mail Send Failure due to " + exception.getMessage());
        }

    }

    @Override
    public void send(final Mail mail) throws MessagingException {
        final MimeMessage message = javaMailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setTo(mail.getToAddress());
        helper.setText(mail.getContent(), true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFromAddress());
        javaMailSender.send(message);
    }

	@Override
	public void sendApplicantFormProcessEmail(ApplicantDto applicant) {
		final Mail mail = new Mail();
        mail.setSubject("Free Assessment Form  - CanNext");
        mail.setToAddress(applicant.getEmailId());
        mail.setFromAddress(mailFrom);
        mail.getModel().put("userEmail", applicant.getEmailId());
        mail.getModel().put("userName", applicant.getFirstName()+" "+ applicant.getLastName());
        try {
            configuration.setClassForTemplateLoading(getClass(), basePackagePath);
            final Template template = configuration.getTemplate("ApplicantForm.ftl");
            final String mailContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
            mail.setContent(mailContent);
            send(mail);
        } catch (final IOException | TemplateException | MessagingException exception) {
            logger.debug("Mail Send Failure due to " + exception.getMessage());
        }
	}


}
