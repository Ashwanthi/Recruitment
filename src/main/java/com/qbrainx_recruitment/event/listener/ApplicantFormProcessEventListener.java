package com.qbrainx_recruitment.event.listener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.qbrainx_recruitment.dto.ApplicantDto;
import com.qbrainx_recruitment.event.ApplicantFormProcessEvent;
import com.qbrainx_recruitment.service.MailService;

@Component
public class ApplicantFormProcessEventListener implements ApplicationListener<ApplicantFormProcessEvent> {

    private static final Logger logger = Logger.getLogger(ApplicantFormProcessEventListener.class);

    private final MailService mailService;

    public ApplicantFormProcessEventListener(final MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    @Async
    public void onApplicationEvent(final ApplicantFormProcessEvent applicantFromProcessEvent) {
        sendApplicantFormEmail(applicantFromProcessEvent);
    }

    private void sendApplicantFormEmail(final ApplicantFormProcessEvent userAccountChangeEvent) {
        final ApplicantDto applicantDetails = userAccountChangeEvent.getApplicantDto();
        mailService.sendApplicantFormProcessEmail(applicantDetails);
    }


}
