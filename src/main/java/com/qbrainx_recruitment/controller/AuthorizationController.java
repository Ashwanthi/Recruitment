package com.qbrainx_recruitment.controller;

import com.qbrainx_recruitment.dto.ApiResponse;
import com.qbrainx_recruitment.dto.AuthenticationResponse;
import com.qbrainx_recruitment.dto.LoginRequestDto;
import com.qbrainx_recruitment.dto.PasswordRestRequestDto;
import com.qbrainx_recruitment.dto.RefreshTokenDto;
import com.qbrainx_recruitment.dto.UsersDto;
import com.qbrainx_recruitment.event.GenerateResetLinkEvent;
import com.qbrainx_recruitment.event.RegenerateEmailVerificationEvent;
import com.qbrainx_recruitment.event.UserAccountChangeEvent;
import com.qbrainx_recruitment.event.UserRegistrationCompleteEvent;
import com.qbrainx_recruitment.exception.BadResourceException;
import com.qbrainx_recruitment.exception.LoginException;
import com.qbrainx_recruitment.exception.ResourceNotFoundException;
import com.qbrainx_recruitment.model.AuthorizeEmail;
import com.qbrainx_recruitment.model.RefreshToken;
import com.qbrainx_recruitment.security.ISecurityPrincipal;
import com.qbrainx_recruitment.security.JwtTokenProvider;
import com.qbrainx_recruitment.security.SecurityPrincipal;
import com.qbrainx_recruitment.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {

    private static final Logger logger = Logger.getLogger(AuthorizationController.class);

    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;
    private final ApplicationEventPublisher applicationEventPublisher;


    public AuthorizationController(final AuthService authService, final JwtTokenProvider tokenProvider,
                                   final ApplicationEventPublisher applicationEventPublisher) {
        this.authService = authService;
        this.tokenProvider = tokenProvider;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @ApiOperation(value = "Checks if the given email is in use")
    @GetMapping("/checkEmailExist")
    public ResponseEntity<Boolean> checkEmailExists(@ApiParam(value = "Email id to check against") @RequestParam("email") final String email){
        return new ResponseEntity<>(authService.emailAlreadyExists(email), HttpStatus.OK);

    }

    @PostMapping("/login")
    @ApiOperation(value = "Logs the user in to the system and return the auth tokens")
    public ResponseEntity authenticateUser(@ApiParam(value = "The LoginRequest payload") @Valid @RequestBody final LoginRequestDto loginRequestDto) {
        final Authentication authentication = authService.authenticateUser(loginRequestDto)
                .orElseThrow(() -> new LoginException("Couldn't login user [" + loginRequestDto + "]"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final ISecurityPrincipal securityPrincipal = new SecurityPrincipal()
                .buildSecurityPrincipal(authentication.getPrincipal());
        return authService.createAndPersistRefreshTokenForDevice(securityPrincipal, loginRequestDto)
                .map(RefreshToken::getToken)
                .map(refreshToken -> {
                    final String jwtToken = authService.generateToken(securityPrincipal);
                    return ResponseEntity.ok(new AuthenticationResponse(jwtToken, refreshToken, tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new LoginException("Couldn't create"));
    }

    @PostMapping("/register")
    @ApiOperation(value = "Registers the user and publishes an event to generate the email verification")
    public ResponseEntity registerUser(@ApiParam(value = "The RegistrationRequest payload") @Valid @RequestBody final UsersDto usersDto) {

        return authService.registerUser(usersDto)
                .map(user -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
                    UserRegistrationCompleteEvent onUserRegistrationCompleteEvent = new UserRegistrationCompleteEvent(user, urlBuilder);
                    applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
                    logger.info("Registered User returned [API]: " + user);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .data("User registered successfully. Check your email for verification")
                            .success(true)
                            .build());
                })
                .orElseThrow(() -> new BadResourceException(usersDto.getEmail(), "Missing user object in database"));
    }

    @PostMapping("/password/resetlink")
    @ApiOperation(value = "Receive the reset link request and publish event to send mail containing the password " +
            "reset link")
    public ResponseEntity resetLink(@ApiParam(value = "The PasswordResetLinkRequest payload") @Valid @RequestBody final Map<String, String> map) {
        if (map.containsKey("email")) {
            return authService.generatePasswordResetToken(map)
                    .map(passwordResetToken -> {
                        UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/password/reset");
                        GenerateResetLinkEvent generateResetLinkMailEvent = new GenerateResetLinkEvent(urlBuilder, passwordResetToken);
                        applicationEventPublisher.publishEvent(generateResetLinkMailEvent);
                        return ResponseEntity.ok(ApiResponse.builder()
                                .data("Password reset link sent successfully")
                                .success(true)
                                .build());
                    })
                    .orElseThrow(() -> new BadResourceException(map.get("email"), "Couldn't create a valid token"));
        } else {
            throw new BadResourceException(map.get("email"), "Couldn't reset link");
        }
    }


    @PostMapping("/password/reset")
    @ApiOperation(value = "Reset the password after verification and publish an event to send the acknowledgement " +
            "email")
    public ResponseEntity resetPassword(@ApiParam(value = "The PasswordResetRequest payload") @Valid @RequestBody
                                            final PasswordRestRequestDto passwordRestRequestDto) {
        return authService.resetPassword(passwordRestRequestDto)
                .map(changedUser -> {
                    UserAccountChangeEvent onPasswordChangeEvent = new UserAccountChangeEvent(changedUser, "Reset Password",
                            "Changed Successfully");
                    applicationEventPublisher.publishEvent(onPasswordChangeEvent);
                    return ResponseEntity.ok(ApiResponse.builder().success(true).data("Password changed successfully").build());
                })
                .orElseThrow(() -> new BadResourceException(passwordRestRequestDto.getToken(), "Error in resetting password"));
    }


    @GetMapping("/registrationConfirmation")
    @ApiOperation(value = "Confirms the email verification token that has been generated for the user during registration")
    public ResponseEntity confirmRegistration(@ApiParam(value = "the token that was sent to the user email") @RequestParam("token") String token) {

        return authService.confirmEmailRegistration(token)
                .map(user ->
                        ResponseEntity.ok(
                                    ApiResponse.builder().data("User verified successfully")
                                            .success(true)
                                            .build()))
                .orElseThrow(() -> new BadResourceException("Email Verification Token Error",
                        "Failed to confirm. Please generate a new email verification request"));
    }


    @GetMapping("/resendRegistrationToken")
    @ApiOperation(value = "Resend the email registration with an updated token expiry. Safe to " +
            "assume that the user would always click on the last re-verification email and " +
            "any attempts at generating new token from past (possibly archived/deleted)" +
            "tokens should fail and report an exception. ")
    public ResponseEntity resendRegistrationToken(@ApiParam(value = "the initial token that was sent to the user email after registration")
                                                      @RequestParam("token") final String existingToken) {

        final AuthorizeEmail newEmailToken = authService.recreateRegistrationToken(existingToken)
                .orElseThrow(() -> new BadResourceException("Email Verification Token", "User is already registered. No need to re-generate token"));

        return Optional.ofNullable(newEmailToken.getUsers())
                .map(registeredUser -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
                    RegenerateEmailVerificationEvent regenerateEmailVerificationEvent = new RegenerateEmailVerificationEvent(registeredUser, urlBuilder, newEmailToken);
                    applicationEventPublisher.publishEvent(regenerateEmailVerificationEvent);
                    return ResponseEntity.ok(ApiResponse.builder()
                            .success(true)
                            .data("Email verification resent successfully")
                            .build());
                })
                .orElseThrow(() -> new BadResourceException("No user associated with this request. Re-verification denied", ""));
    }


    @PostMapping("/refresh")
    @ApiOperation(value = "Refresh the expired jwt authentication by issuing a token refresh request and returns the" +
            "updated response tokens")
    public ResponseEntity refreshJwtToken(@ApiParam(value = "The TokenRefreshRequest payload") @Valid @RequestBody final RefreshTokenDto refreshTokenDto) {

        return authService.refreshJwtToken(refreshTokenDto)
                .map(updatedToken -> {
                    String refreshToken = refreshTokenDto.getRefreshToken();
                    logger.info("Created new Jwt Auth token: " + updatedToken);
                    return ResponseEntity.ok(new AuthenticationResponse(updatedToken, refreshToken, tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new BadResourceException(refreshTokenDto.getRefreshToken(),
                        "Unexpected error during token refresh. Please logout and login again."));
    }
}
