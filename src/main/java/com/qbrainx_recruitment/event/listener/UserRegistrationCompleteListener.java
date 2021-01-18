package com.qbrainx_recruitment.event.listener;

import com.qbrainx_recruitment.dto.UsersDto;
import com.qbrainx_recruitment.event.UserRegistrationCompleteEvent;
import com.qbrainx_recruitment.model.Users;
import com.qbrainx_recruitment.service.AuthorizeEmailService;
import com.qbrainx_recruitment.service.MailService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationCompleteListener implements ApplicationListener<UserRegistrationCompleteEvent> {

    private static final Logger logger = Logger.getLogger(UserRegistrationCompleteEvent.class);

    private final AuthorizeEmailService authorizeEmailService;
    private final MailService mailService;

    public UserRegistrationCompleteListener(final AuthorizeEmailService authorizeEmailService,
                                            final MailService mailService) {
        this.authorizeEmailService = authorizeEmailService;
        this.mailService = mailService;
    }

    @Override
    @Async
    public void onApplicationEvent(final UserRegistrationCompleteEvent userRegistrationCompleteEvent) {
        sendEmailVerification(userRegistrationCompleteEvent);
    }

    private void sendEmailVerification(final UserRegistrationCompleteEvent userRegistrationCompleteEvent) {
        final UsersDto usersDto = userRegistrationCompleteEvent.getUsersDto();
        final String token = authorizeEmailService.generateNewToken();
        authorizeEmailService.createVerificationToken(usersDto, token);
        final String recipientAddress = usersDto.getEmail();
        final String emailConfirmationUrl = userRegistrationCompleteEvent.getRedirectUrl()
                .queryParam("token", token).toUriString();
        mailService.sendEmailVerification(emailConfirmationUrl, recipientAddress);
    }
}
