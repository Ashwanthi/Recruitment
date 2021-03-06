package com.qbrainx_recruitment.repository;

import com.qbrainx_recruitment.model.AuthorizeEmail;
import com.qbrainx_recruitment.model.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByToken(String token);
}
