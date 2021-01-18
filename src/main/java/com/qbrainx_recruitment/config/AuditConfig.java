package com.qbrainx_recruitment.config;

import com.qbrainx_recruitment.security.ISecurityContext;
import com.qbrainx_recruitment.security.ISecurityPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class AuditConfig {

    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
    }

    @Bean
    public AuditorAware<Long> auditorProvider(final ISecurityContext securityContext) {
        return () -> securityContext.getOptionalPrincipal().map(ISecurityPrincipal::getId);
    }
}
