package com.qbrainx_recruitment.event.listener;

import com.qbrainx_recruitment.cache.LoggedOutJwtTokenCache;
import com.qbrainx_recruitment.event.UserLogoutSuccessEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class LogoutSuccessEventListener implements ApplicationListener<UserLogoutSuccessEvent> {

    private final LoggedOutJwtTokenCache loggedOutJwtTokenCache;

    public LogoutSuccessEventListener(final LoggedOutJwtTokenCache loggedOutJwtTokenCache) {
        this.loggedOutJwtTokenCache = loggedOutJwtTokenCache;
    }


    @Override
    public void onApplicationEvent(final UserLogoutSuccessEvent userLogoutSuccessEvent) {
        if (null != userLogoutSuccessEvent) {
            loggedOutJwtTokenCache.markLogoutEventForToken(userLogoutSuccessEvent);
        }
    }
}
