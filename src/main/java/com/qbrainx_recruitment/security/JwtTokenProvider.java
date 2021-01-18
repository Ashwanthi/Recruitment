package com.qbrainx_recruitment.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final String jwtSecret;
    private final long jwtExpirationInSeconds;

    public JwtTokenProvider(@Value("${app.jwt.secret}") final String jwtSecret,
                            @Value("${app.jwt.expiration}") final long jwtExpirationInSeconds) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationInSeconds = jwtExpirationInSeconds;
    }

    public String generateToken(final ISecurityPrincipal securityPrincipal) {
        return Jwts.builder()
                .setClaims(buildClaimMap(securityPrincipal))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(jwtExpirationInSeconds)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Long getUserIdFromJWT(final String jwtToken) {
        final Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwtToken)
                .getBody();
        return Long.parseLong(String.valueOf(claims.get("id")));
    }

    public Date getTokenExpiryFromJWT(final String jwtToken) {
        final Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwtToken)
                .getBody();

        return claims.getExpiration();
    }

    public long getExpiryDuration() {
        return jwtExpirationInSeconds;
    }

    private Map<String, Object> buildClaimMap(final ISecurityPrincipal iSecurityPrincipal) {
        final Map<String, Object> map = new HashMap<>();
        map.put("id", iSecurityPrincipal.getId());
        map.put("name", iSecurityPrincipal.getName());
        map.put("role", iSecurityPrincipal.getRole());
        map.put("privileges", iSecurityPrincipal.getPrivileges());
        return map;
    }

}
