package com.qbrainx_recruitment.service;

import com.qbrainx_recruitment.dto.UsersDto;
import com.qbrainx_recruitment.model.AuthorizeEmail;
import com.qbrainx_recruitment.model.Users;

import java.util.Optional;
import java.util.UUID;

public interface AuthorizeEmailService {


    void createVerificationToken(UsersDto usersDto, String token);

    String generateNewToken();

    AuthorizeEmail updateExistingTokenWithNameAndExpiry(AuthorizeEmail authorizeEmail);

    Optional<AuthorizeEmail> findByToken(String token);

    void verifyExpiration(AuthorizeEmail authorizeEmail);

    AuthorizeEmail save(AuthorizeEmail authorizeEmail);
}
