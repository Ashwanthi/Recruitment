package com.qbrainx_recruitment.security;

import com.qbrainx_recruitment.cache.LoggedOutJwtTokenCache;
import com.qbrainx_recruitment.event.UserLogoutSuccessEvent;
import com.qbrainx_recruitment.exception.InvalidTokenRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenValidator {

    private static final Logger logger = Logger.getLogger(JwtTokenValidator.class);

    private final String jwtSecret;

    private final LoggedOutJwtTokenCache loggedOutTokenCache;


    public JwtTokenValidator(@Value("${app.jwt.secret}") final String jwtSecret,
                             final LoggedOutJwtTokenCache loggedOutTokenCache) {
        this.jwtSecret = jwtSecret;
        this.loggedOutTokenCache = loggedOutTokenCache;
    }

    //@Todo: Mark the entry for the logged out token
    public boolean validateJwt(final String jwtToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
        } catch (final SignatureException ex) {
            throw new InvalidTokenRequestException("JWT", jwtToken, "Incorrect signature");

        } catch (final MalformedJwtException ex) {
            throw new InvalidTokenRequestException("JWT", jwtToken, "Malformed jwt token");

        } catch (final ExpiredJwtException ex) {
            throw new InvalidTokenRequestException("JWT", jwtToken, "Token expired. Refresh required");

        } catch (final UnsupportedJwtException ex) {
            throw new InvalidTokenRequestException("JWT", jwtToken, "Unsupported JWT token");

        } catch (final IllegalArgumentException ex) {
            throw new InvalidTokenRequestException("JWT", jwtToken, "Illegal argument token");
        }
        validateTokenIsNotForALoggedOutDevice(jwtToken);
        return true;
    }

    private void validateTokenIsNotForALoggedOutDevice(final String jwtToken) {
        final UserLogoutSuccessEvent previouslyLoggedOutEvent = loggedOutTokenCache.getUserLogoutSuccessEvent(jwtToken);
        if (previouslyLoggedOutEvent != null) {
            final String userEmail = previouslyLoggedOutEvent.getUserEmail();
            final Date logoutEventDate = previouslyLoggedOutEvent.getEventTime();
            final String errorMessage = String.format("Token corresponds to an already logged out user [%s] at [%s]. Please login again",
                    userEmail, logoutEventDate);
            throw new InvalidTokenRequestException("JWT", jwtToken, errorMessage);
        }
    }
}
