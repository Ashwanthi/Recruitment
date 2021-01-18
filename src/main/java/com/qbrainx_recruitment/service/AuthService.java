package com.qbrainx_recruitment.service;

import com.qbrainx_recruitment.dto.LoginRequestDto;
import com.qbrainx_recruitment.dto.PasswordRestRequestDto;
import com.qbrainx_recruitment.dto.RefreshTokenDto;
import com.qbrainx_recruitment.dto.RegisterRequestDto;
import com.qbrainx_recruitment.dto.UsersDto;
import com.qbrainx_recruitment.model.AuthorizeEmail;
import com.qbrainx_recruitment.model.PasswordReset;
import com.qbrainx_recruitment.model.RefreshToken;
import com.qbrainx_recruitment.model.Users;
import com.qbrainx_recruitment.security.ISecurityPrincipal;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.Optional;

public interface AuthService {

    Boolean emailAlreadyExists(String email);

    Optional<UsersDto> registerUser(UsersDto usersDto);

    Optional<Authentication> authenticateUser(LoginRequestDto userLoginRequestDto);

    Optional<Users> confirmEmailRegistration(String emailToken);

    Optional<AuthorizeEmail> recreateRegistrationToken(String existingToken);

    Boolean currentPasswordMatches(Users currentUser, String password);

    ISecurityPrincipal buildISecurityPrincipal(Authentication authentication);

    // Optional<Users> updatePassword(CustomUserDetails customUserDetails,  UpdatePasswordRequest updatePasswordRequest);

    String generateToken(ISecurityPrincipal securityPrincipal);

    String generateTokenFromUserId(Long userId);

    Optional<RefreshToken> createAndPersistRefreshTokenForDevice(ISecurityPrincipal iSecurityPrincipal, LoginRequestDto loginRequest);

    Optional<String> refreshJwtToken(RefreshTokenDto refreshTokenDto);

    Optional<PasswordReset> generatePasswordResetToken(Map<String, String> map);

    Optional<Users> resetPassword(PasswordRestRequestDto passwordResetRequest);

}
