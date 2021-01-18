package com.qbrainx_recruitment.cache;

import com.qbrainx_recruitment.event.UserLogoutSuccessEvent;
import com.qbrainx_recruitment.security.JwtTokenProvider;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class LoggedOutJwtTokenCache {

    private static final Logger logger = Logger.getLogger(LoggedOutJwtTokenCache.class);

    private final ExpiringMap<String, UserLogoutSuccessEvent> tokenEventMap;

    private final JwtTokenProvider tokenProvider;

    public LoggedOutJwtTokenCache(@Value("${app.cache.logoutToken.maxSize}") final int maxSize,
                                  final JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
        this.tokenEventMap = ExpiringMap.builder()
                .variableExpiration()
                .maxSize(maxSize)
                .build();
    }

    public void markLogoutEventForToken(final UserLogoutSuccessEvent userLogoutSuccessEvent) {
        final String token = userLogoutSuccessEvent.getToken();
        if (tokenEventMap.containsKey(token)) {
            logger.info(String.format("Log out token for user [%s] is already present in the cache",
                    userLogoutSuccessEvent.getUserEmail()));
        } else {
            final Date tokenExpiryDate = tokenProvider.getTokenExpiryFromJWT(token);
            final long ttlForToken = getTTLForToken(tokenExpiryDate);
            tokenEventMap.put(token, userLogoutSuccessEvent, ttlForToken, TimeUnit.SECONDS);
        }
    }

    public UserLogoutSuccessEvent getUserLogoutSuccessEvent(final String token) {
        return tokenEventMap.get(token);
    }

    private long getTTLForToken(final Date date) {
        final long secondAtExpiry = date.toInstant().getEpochSecond();
        final long secondAtLogout = Instant.now().getEpochSecond();
        return Math.max(0, secondAtExpiry - secondAtLogout);
    }
}
