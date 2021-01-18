package com.qbrainx_recruitment.security;

import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface ISecurityContext {

    Optional<ISecurityPrincipal> getOptionalPrincipal();

    ISecurityPrincipal getPrincipal();

    Authentication getAuthentication();
}
