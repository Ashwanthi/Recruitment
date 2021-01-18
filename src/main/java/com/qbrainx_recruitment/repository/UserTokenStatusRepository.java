package com.qbrainx_recruitment.repository;

import com.qbrainx_recruitment.model.RefreshToken;
import com.qbrainx_recruitment.model.UserTokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenStatusRepository extends JpaRepository<UserTokenStatus, Long> {

    Optional<UserTokenStatus> findByUsersId(Long userId);

    Optional<UserTokenStatus> findByRefreshToken(RefreshToken refreshToken);

}
