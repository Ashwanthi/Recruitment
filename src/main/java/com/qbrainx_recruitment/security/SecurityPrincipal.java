package com.qbrainx_recruitment.security;

import com.qbrainx_recruitment.model.CustomUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SecurityPrincipal implements ISecurityPrincipal {

    private Long id;

    private String name;

    private String role;

    private Set<String> privileges;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getRole() {
        return this.role;
    }

    @Override
    public Set<String> getPrivileges() {
        return this.privileges;
    }

    public <T> List<T> getAuthority(final Function<String, T> mapper) {
        return getPrivileges().stream()
                .map(mapper)
                .collect(Collectors.toList());
    }


}
