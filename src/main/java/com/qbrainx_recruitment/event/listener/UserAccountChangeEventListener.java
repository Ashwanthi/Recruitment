package com.qbrainx_recruitment.event.listener;

import com.qbrainx_recruitment.event.UserAccountChangeEvent;
import com.qbrainx_recruitment.model.Users;
import com.qbrainx_recruitment.service.MailService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

public class UserAccountChangeEventListener implements ApplicationListener<UserAccountChangeEvent> {

    private static final Logger logger = Logger.getLogger(UserAccountChangeEventListener.class);

    private final MailService mailService;

    public UserAccountChangeEventListener(final MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    @Async
    public void onApplicationEvent(final UserAccountChangeEvent userAccountChangeEvent) {
        sendAccountChangeEmail(userAccountChangeEvent);
    }

    private void sendAccountChangeEmail(final UserAccountChangeEvent userAccountChangeEvent) {
        final Users user = userAccountChangeEvent.getUsers();
        final String action = userAccountChangeEvent.getAction();
        final String actionStatus = userAccountChangeEvent.getActionStatus();
        final String recipientAddress = user.getEmail();
        mailService.sendAccountChangeEmail(action, actionStatus, recipientAddress);
    }
}
