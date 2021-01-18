package com.qbrainx_recruitment.serviceImpl;

import com.qbrainx_recruitment.dto.UsersDto;
import com.qbrainx_recruitment.exception.BadResourceException;
import com.qbrainx_recruitment.model.AuthorizeEmail;
import com.qbrainx_recruitment.model.TokenStatus;
import com.qbrainx_recruitment.model.Users;
import com.qbrainx_recruitment.repository.AuthorizeEmailRepository;
import com.qbrainx_recruitment.repository.UserRepository;
import com.qbrainx_recruitment.service.AuthorizeEmailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorizeEmailServiceImpl implements AuthorizeEmailService {

    private static final Logger logger = Logger.getLogger(AuthorizeEmailServiceImpl.class);

    @Value("${app.token.email.verification.duration}")
    private Long emailVerificationTokenExpiryDuration;

    private final UserRepository userRepository;

    private final AuthorizeEmailRepository authorizeEmailRepository;

    public AuthorizeEmailServiceImpl(final UserRepository userRepository, final AuthorizeEmailRepository authorizeEmailRepository) {
        this.userRepository = userRepository;
        this.authorizeEmailRepository = authorizeEmailRepository;
    }

    @Override
    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public AuthorizeEmail updateExistingTokenWithNameAndExpiry(final AuthorizeEmail authorizeEmail) {
        authorizeEmail.setTokenStatus(TokenStatus.STATUS_PENDING);
        authorizeEmail.setExpiryAt(LocalDateTime.now().plusSeconds(emailVerificationTokenExpiryDuration));
        return save(authorizeEmail);
    }

    @Override
    public void createVerificationToken(final UsersDto usersDto, final String token) {
        final Optional<Users> users = userRepository.findById(usersDto.getId());
        if (users.isPresent()) {
            final AuthorizeEmail authorizeEmail = new AuthorizeEmail();
            authorizeEmail.setToken(token);
            authorizeEmail.setTokenStatus(TokenStatus.STATUS_PENDING);
            authorizeEmail.setUsers(users.get());
            authorizeEmail.setExpiryAt(LocalDateTime.now().plusSeconds(emailVerificationTokenExpiryDuration));
            authorizeEmailRepository.save(authorizeEmail);
            logger.info("Generated Email verification token [" + usersDto.getEmail() + "]");
        } else {
            logger.debug("Create Verificaiton Token Failure for the user " + usersDto.getEmail());
        }
    }

    @Override
    public Optional<AuthorizeEmail> findByToken(final String token) {
        return authorizeEmailRepository.findByToken(token);
    }

    @Override
    public void verifyExpiration(final AuthorizeEmail authorizeEmail) {
        if (authorizeEmail.getExpiryAt().compareTo(LocalDateTime.now()) < 0) {
            throw new BadResourceException( "Expired token. Please issue a new request", "Expired");
        }
    }

    @Override
    public AuthorizeEmail save(final AuthorizeEmail authorizeEmail) {
        return authorizeEmailRepository.save(authorizeEmail);
    }
}
