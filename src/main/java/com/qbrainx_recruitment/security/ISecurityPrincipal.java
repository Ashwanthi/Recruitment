package com.qbrainx_recruitment.security;

import com.qbrainx_recruitment.security.SecurityPrincipal;
import com.qbrainx_recruitment.model.CustomUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Set;
import java.util.stream.Collectors;

public interface ISecurityPrincipal {

    Long getId();

    String getName();

    String getRole();

    Set<String> getPrivileges();

    default ISecurityPrincipal buildSecurityPrincipal(final Object object) {
        final CustomUser customUser = (CustomUser) object;
        final Set<String> privilege = customUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return SecurityPrincipal.builder()
                .id(customUser.getId())
                .name(customUser.getFirstName())
                .role(customUser.getRoleFromChild())
                .privileges(privilege)
                .build();

    }

    default ISecurityPrincipal buildSecurityPrincipalPostLogin(final PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken) {
        final CustomUser customUser = (CustomUser) preAuthenticatedAuthenticationToken.getPrincipal();
        final Set<String> privilege = customUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return SecurityPrincipal.builder()
                .id(customUser.getId())
                .name(customUser.getFirstName())
                .role(customUser.getRoleFromChild())
                .privileges(privilege)
                .build();

    }
}
