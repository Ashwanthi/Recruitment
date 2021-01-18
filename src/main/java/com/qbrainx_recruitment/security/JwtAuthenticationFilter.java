package com.qbrainx_recruitment.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.qbrainx_recruitment.serviceImpl.CustomUserService;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //implements Filter {

    private static final Logger LOGGER = Logger.getLogger(JwtAuthenticationFilter.class);

    @Value("${app.jwt.header}")
    private String tokenRequestHeader;

    @Value("${app.jwt.header.prefix}")
    private String tokenRequestHeaderPrefix;

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserService customUserService;

   @Override
    protected void doFilterInternal(final HttpServletRequest httpServletRequest,
                                    final HttpServletResponse httpServletResponse,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            final Optional<String> jwtToken = getJwtFromToken(httpServletRequest);

            if (jwtToken.isPresent() && StringUtils.hasText(jwtToken.get()) &&
                    jwtTokenValidator.validateJwt(jwtToken.get())) {

                final Long userId = jwtTokenProvider.getUserIdFromJWT(jwtToken.get());

                final UserDetails userDetails = customUserService.loadUserById(userId);
                final PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(userDetails,
                        jwtToken.get(),
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (final ResponseStatusException ex) {
            SecurityContextHolder.clearContext();
            LOGGER.error("Failed to set user authentication in security context: ", ex);
            httpServletResponse.sendError(ex.getStatus().value(), ex.getMessage());
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    private Optional<String> getJwtFromToken(final HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(tokenRequestHeader);
        if (!StringUtils.isEmpty(authorizationHeader) && authorizationHeader.startsWith(tokenRequestHeaderPrefix)) {
            return Optional.of(authorizationHeader.replace(tokenRequestHeaderPrefix, ""));
        }
        return Optional.empty();
    }

   /* @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        try {
            final Optional<String> jwtToken = getJwtFromToken(httpRequest);

            if (jwtToken.isPresent() && StringUtils.hasText(jwtToken.get()) &&
                    jwtTokenValidator.validateJwt(jwtToken.get())) {

                final Long userId = jwtTokenProvider.getUserIdFromJWT(jwtToken.get());

                final UserDetails userDetails = customUserService.loadUserById(userId);
                final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        jwtToken.get(),
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch (final ResponseStatusException ex) {
            SecurityContextHolder.clearContext();
            LOGGER.error("Failed to set user authentication in security context: ", ex);
            httpResponse.sendError(ex.getStatus().value(), ex.getMessage());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }*/
}
