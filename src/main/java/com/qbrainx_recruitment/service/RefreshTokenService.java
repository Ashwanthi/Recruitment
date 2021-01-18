package com.qbrainx_recruitment.service;

import com.qbrainx_recruitment.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken save(RefreshToken refreshToken);

    RefreshToken createRefreshToken();

    void verifyExpiration(RefreshToken refreshToken);

    void deleteById(Long id);

    void increaseCount(RefreshToken refreshToken);

}
