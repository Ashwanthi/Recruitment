package com.qbrainx_recruitment.service;

import com.qbrainx_recruitment.model.RefreshToken;
import com.qbrainx_recruitment.model.UserTokenStatus;
import com.qbrainx_recruitment.model.Users;

import java.util.Optional;

public interface UserTokenStatusService {

    Optional<UserTokenStatus> findByUserId(Long userId);

    UserTokenStatus createUserTokenStatus(Users users);

    void verifyRefreshAvailability(RefreshToken refreshToken);

    Optional<UserTokenStatus> findByRefreshToken(RefreshToken refreshToken);
}
