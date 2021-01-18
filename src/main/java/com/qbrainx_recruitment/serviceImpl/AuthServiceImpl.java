package com.qbrainx_recruitment.serviceImpl;

import com.qbrainx_recruitment.dto.LoginRequestDto;
import com.qbrainx_recruitment.dto.PasswordRestRequestDto;
import com.qbrainx_recruitment.dto.RefreshTokenDto;
import com.qbrainx_recruitment.dto.UsersDto;
import com.qbrainx_recruitment.exception.BadResourceException;
import com.qbrainx_recruitment.exception.ResourceAlreadyExistsException;
import com.qbrainx_recruitment.exception.ResourceNotFoundException;
import com.qbrainx_recruitment.model.AuthorizeEmail;
import com.qbrainx_recruitment.model.CustomUser;
import com.qbrainx_recruitment.model.PasswordReset;
import com.qbrainx_recruitment.model.RefreshToken;
import com.qbrainx_recruitment.model.TokenStatus;
import com.qbrainx_recruitment.model.UserTokenStatus;
import com.qbrainx_recruitment.model.Users;
import com.qbrainx_recruitment.security.ISecurityContext;
import com.qbrainx_recruitment.security.ISecurityPrincipal;
import com.qbrainx_recruitment.security.JwtTokenProvider;
import com.qbrainx_recruitment.security.SecurityPrincipal;
import com.qbrainx_recruitment.service.AuthService;
import com.qbrainx_recruitment.service.AuthorizeEmailService;
import com.qbrainx_recruitment.service.PasswordResetService;
import com.qbrainx_recruitment.service.RefreshTokenService;
import com.qbrainx_recruitment.service.UserService;
import com.qbrainx_recruitment.service.UserTokenStatusService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = Logger.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final AuthorizeEmailService authorizeEmailService;

    private final UserTokenStatusService userTokenStatusService;

    private final RefreshTokenService refreshTokenService;

    private final JwtTokenProvider tokenProvider;

    private final PasswordResetService passwordResetService;

    private final ISecurityContext securityContext;

    public AuthServiceImpl(final AuthenticationManager authenticationManager,
                           final UserService userService,
                           final AuthorizeEmailService authorizeEmailService,
                           final RefreshTokenService refreshTokenService,
                           final UserTokenStatusService userTokenStatusService,
                           final JwtTokenProvider tokenProvider, final PasswordResetService passwordResetService, final ISecurityContext securityContext) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.authorizeEmailService = authorizeEmailService;
        this.userTokenStatusService = userTokenStatusService;
        this.refreshTokenService = refreshTokenService;
        this.tokenProvider = tokenProvider;
        this.passwordResetService = passwordResetService;
        this.securityContext = securityContext;
    }

    @Override
    public Boolean emailAlreadyExists(final String email) {
        return userService.existsByEmail(email);
    }

    @Override
    public Optional<UsersDto> registerUser(final UsersDto usersDto) {
        final String email = usersDto.getEmail();
        if (emailAlreadyExists(email)) {
            logger.error("Email already exists: " + email);
            throw new ResourceAlreadyExistsException("Email", email );
        }
        logger.info("Register the new User [" + email + "]");
        usersDto.setIsEmailVerified(Boolean.FALSE);
        return Optional.of(userService.save(usersDto));
    }

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public Optional<Authentication> authenticateUser(final LoginRequestDto userLoginRequestDto) {
    	
    	return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequestDto.getEmail(),
                        userLoginRequestDto.getPassword())));
    }

    @Override
    public Optional<Users> confirmEmailRegistration(final String emailToken) {
        final AuthorizeEmail authorizeEmail = authorizeEmailService.findByToken(emailToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token", "Email verification"));

        final Users users = authorizeEmail.getUsers();
        if (users.getIsEmailVerified()) {
            logger.info("User [" + emailToken + "] already registered.");
            return Optional.of(users);
        }

        authorizeEmailService.verifyExpiration(authorizeEmail);
        authorizeEmail.setTokenStatus(TokenStatus.STATUS_CONFIRMED);
        authorizeEmailService.save(authorizeEmail);
        users.setIsEmailVerified(Boolean.TRUE);
        userService.saveUsers(users);
        return Optional.of(users);
    }

    @Override
    public Optional<AuthorizeEmail> recreateRegistrationToken(final String existingToken) {
        final AuthorizeEmail emailVerificationToken = authorizeEmailService.findByToken(existingToken)
                .orElseThrow(() -> new ResourceNotFoundException("Existing email verification", existingToken));

        if (emailVerificationToken.getUsers().getIsEmailVerified()) {
            return Optional.empty();
        }
        return Optional.ofNullable(authorizeEmailService.updateExistingTokenWithNameAndExpiry(emailVerificationToken));
    }

    @Override
    public Boolean currentPasswordMatches(final Users currentUser, final String password) {
        return null;
    }

    @Override
    public ISecurityPrincipal buildISecurityPrincipal(final Authentication authentication) {
        final CustomUser customUser = (CustomUser) authentication.getPrincipal();
        final Set<String> privilege = customUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return SecurityPrincipal.builder()
                .id(customUser.getId())
                .name(customUser.getFirstName())
                .role(customUser.getRoleFromChild())
                .privileges(privilege)
                .build();
    }

    @Override
    public String generateToken(final ISecurityPrincipal securityPrincipal) {
        return tokenProvider.generateToken(securityPrincipal);
    }

    @Override
    public String generateTokenFromUserId(final Long userId) {
        if (userId.equals(securityContext.getPrincipal().getId())) {
            return tokenProvider.generateToken(securityContext.getPrincipal());
        }
        throw new BadResourceException("Can't Issue refresh token", "");
    }

    @Override
    public Optional<RefreshToken> createAndPersistRefreshTokenForDevice(final ISecurityPrincipal securityPrincipal,
                                                                        final LoginRequestDto loginRequest) {
        final Users users = userService.findById(securityPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User is Not Available", ""));
        userTokenStatusService.findByUserId(users.getId())
                .map(UserTokenStatus::getRefreshToken)
                .map(RefreshToken::getId)
                .ifPresent(refreshTokenService::deleteById);

        final UserTokenStatus userTokenStatus = userTokenStatusService.createUserTokenStatus(users);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken();
        userTokenStatus.setRefreshToken(refreshToken);
        refreshToken.setUserTokenStatus(userTokenStatus);
        refreshToken = refreshTokenService.save(refreshToken);
        return Optional.ofNullable(refreshToken);
    }

    @Override
    public Optional<String> refreshJwtToken(final RefreshTokenDto refreshTokenDto) {
        final String token = refreshTokenDto.getRefreshToken();
        return Optional.of(refreshTokenService.findByToken(token)
                .map(refreshToken -> {
                    refreshTokenService.verifyExpiration(refreshToken);
                    userTokenStatusService.verifyRefreshAvailability(refreshToken);
                    refreshTokenService.increaseCount(refreshToken);
                    return refreshToken;
                })
                .map(RefreshToken::getUserTokenStatus)
                .map(UserTokenStatus::getUsers)
                .map(Users::getId)
                .map(this::generateTokenFromUserId))
                .orElseThrow(() -> new BadResourceException("Missing Refresh Token, please login again", ""));

    }

    @Override
    public Optional<PasswordReset> generatePasswordResetToken(final Map<String, String> map) {
        final String email = map.get("email");
        return userService.findByEmail(email)
                .map(users -> {
                    final PasswordReset passwordResetToken = passwordResetService.createToken();
                    passwordResetToken.setUsers(users);
                    passwordResetService.save(passwordResetToken);
                    return Optional.of(passwordResetToken);
                })
                .orElseThrow(() -> new BadResourceException(email, "No matching user found for the given request"));

    }

    @Override
    public Optional<Users> resetPassword(final PasswordRestRequestDto passwordResetRequestDto) {
        final String token = passwordResetRequestDto.getToken();
        final PasswordReset passwordReset = passwordResetService.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Password Reset request is invalid", "Token Id"));
        final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        passwordResetService.verifyExpiration(passwordReset);
        final String encodedPassword = passwordEncoder.encode(passwordResetRequestDto.getPassword());

        return Optional.of(passwordReset)
                .map(PasswordReset::getUsers)
                .map(user -> {
                    user.setPassword(encodedPassword);
                    UsersDto usersDto = new UsersDto();
                    BeanUtils.copyProperties(user, usersDto);
                    usersDto = userService.save(usersDto);
                    BeanUtils.copyProperties(usersDto, user);
                    return user;
                });
    }
}
