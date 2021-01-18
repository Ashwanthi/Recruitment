package com.qbrainx_recruitment.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;

import com.qbrainx_recruitment.security.SecurityPrincipal;

import java.util.Optional;

public class SecurityContext implements ISecurityContext {

    @Override
    public Optional<ISecurityPrincipal> getOptionalPrincipal() {
        return getOptionalAuthentication()
                .map(Authentication::getPrincipal)
                .map(o -> new SecurityPrincipal().buildSecurityPrincipal(o));
    }

    @Override
    public ISecurityPrincipal getPrincipal() {
        return new SecurityPrincipal().buildSecurityPrincipalPostLogin((PreAuthenticatedAuthenticationToken) getAuthentication());
        //return (ISecurityPrincipal) getAuthentication().getPrincipal();
    }

    @Override
    public Authentication getAuthentication() {
        return getOptionalAuthentication()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication not found"));
    }

    private Optional<Authentication> getOptionalAuthentication() {
        return Optional.ofNullable(SecurityContextHolder
                .getContext())
                .map(org.springframework.security.core.context.SecurityContext::getAuthentication);
    }
}
