package com.qbrainx_recruitment.service;

import com.qbrainx_recruitment.model.PasswordReset;

import java.util.Optional;

public interface PasswordResetService {

    PasswordReset save(PasswordReset passwordResetToken);

    Optional<PasswordReset> findByToken(String token);

    PasswordReset createToken();

    void verifyExpiration(PasswordReset token);
}
