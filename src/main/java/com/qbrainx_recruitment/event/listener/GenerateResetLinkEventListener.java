package com.qbrainx_recruitment.event.listener;

import com.qbrainx_recruitment.event.GenerateResetLinkEvent;
import com.qbrainx_recruitment.model.PasswordReset;
import com.qbrainx_recruitment.model.Users;
import com.qbrainx_recruitment.service.MailService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

@Component
public class GenerateResetLinkEventListener implements ApplicationListener<GenerateResetLinkEvent> {

    private static final Logger logger = Logger.getLogger(GenerateResetLinkEventListener.class);

    private final MailService mailService;

    public GenerateResetLinkEventListener(final MailService mailService) {
        this.mailService = mailService;
    }


    @Override
    @Async
    public void onApplicationEvent(final GenerateResetLinkEvent generateResetLinkEvent) {
        sendRestLink(generateResetLinkEvent);
    }

    private void sendRestLink(final GenerateResetLinkEvent generateResetLinkEvent) {
        final PasswordReset passwordReset = generateResetLinkEvent.getPasswordReset();
        final Users user = passwordReset.getUsers();
        final String recipientAddress = user.getEmail();
        final String mailConfirmationUrl = generateResetLinkEvent.getRedirectUrl()
                .queryParam("token", passwordReset.getToken()).toUriString();
        mailService.sendResetLink(mailConfirmationUrl, recipientAddress);
    }
}
