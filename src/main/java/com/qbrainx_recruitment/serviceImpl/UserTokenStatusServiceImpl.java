package com.qbrainx_recruitment.serviceImpl;

import com.qbrainx_recruitment.exception.BadResourceException;
import com.qbrainx_recruitment.model.RefreshToken;
import com.qbrainx_recruitment.model.UserTokenStatus;
import com.qbrainx_recruitment.model.Users;
import com.qbrainx_recruitment.repository.UserTokenStatusRepository;
import com.qbrainx_recruitment.service.UserTokenStatusService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTokenStatusServiceImpl implements UserTokenStatusService {

    private final UserTokenStatusRepository userTokenStatusRepository;


    public UserTokenStatusServiceImpl(final UserTokenStatusRepository userTokenStatusRepository) {
        this.userTokenStatusRepository = userTokenStatusRepository;
    }

    @Override
    public Optional<UserTokenStatus> findByUserId(final Long userId) {
        return userTokenStatusRepository.findByUsersId(userId);
    }

    @Override
    public UserTokenStatus createUserTokenStatus(final Users users) {
        final UserTokenStatus userTokenStatus = new UserTokenStatus();
        userTokenStatus.setUsers(users);
        userTokenStatus.setIsRefreshTokenActive(Boolean.TRUE);
        return userTokenStatus;
    }

    @Override
    public void verifyRefreshAvailability(final RefreshToken refreshToken) {
        final UserTokenStatus userTokenStatus = findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BadResourceException("Refresh Token is not present", ""));
        if (!userTokenStatus.getIsRefreshTokenActive()) {
            throw new BadResourceException("Refresh Token is Expired, please login again", " ");
        }
    }

    @Override
    public Optional<UserTokenStatus> findByRefreshToken(final RefreshToken refreshToken) {
        return userTokenStatusRepository.findByRefreshToken(refreshToken);
    }
}
