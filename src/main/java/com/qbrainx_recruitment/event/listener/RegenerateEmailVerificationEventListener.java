package com.qbrainx_recruitment.event.listener;

import com.qbrainx_recruitment.event.RegenerateEmailVerificationEvent;
import com.qbrainx_recruitment.model.AuthorizeEmail;
import com.qbrainx_recruitment.model.Users;
import com.qbrainx_recruitment.service.MailService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RegenerateEmailVerificationEventListener implements ApplicationListener<RegenerateEmailVerificationEvent> {

    private static final Logger logger = Logger.getLogger(RegenerateEmailVerificationEventListener.class);

    private final MailService mailService;

    public RegenerateEmailVerificationEventListener(final MailService mailService) {
        this.mailService = mailService;
    }


    @Override
    @Async
    public void onApplicationEvent(final RegenerateEmailVerificationEvent regenerateEmailVerificationEvent) {
        resendEmailVerification(regenerateEmailVerificationEvent);
    }

    private void resendEmailVerification(final RegenerateEmailVerificationEvent regenerateEmailVerificationEvent) {
        final Users user = regenerateEmailVerificationEvent.getUsers();
        final AuthorizeEmail authorizeEmail = regenerateEmailVerificationEvent.getAuthorizeEmail();
        final String recipientAddress = user.getEmail();
        final String emailConfirmationUrl = regenerateEmailVerificationEvent.getRedirectUrl()
                        .queryParam("token", authorizeEmail.getToken()).toUriString();
        mailService.sendEmailVerification(emailConfirmationUrl, recipientAddress);
    }
}
