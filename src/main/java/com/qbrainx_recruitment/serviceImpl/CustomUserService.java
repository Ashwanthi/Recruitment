package com.qbrainx_recruitment.serviceImpl;

import com.qbrainx_recruitment.model.CustomUser;
import com.qbrainx_recruitment.model.Users;
import com.qbrainx_recruitment.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserService implements UserDetailsService {
    private static final Logger logger = Logger.getLogger(CustomUserService.class);
    private final UserRepository userRepository;

    public CustomUserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final Optional<Users> dbUser = userRepository.findByEmail(email);
        logger.info("Fetched user : " + dbUser + " by " + email);
        return dbUser.map(users -> {
            final CustomUser customUser =  new CustomUser();
            customUser.setId(users.getId());
            customUser.setPassword(users.getPassword());
            customUser.setEmail(users.getEmail());
            customUser.setActive(users.getActive());
            customUser.setGender(users.getGender());
            customUser.setFirstName(users.getFirstName());
            customUser.setLastName(users.getLastName());
            customUser.setIsEmailVerified(users.getIsEmailVerified());
            customUser.setRole(users.getRole());
            //@Todo: Set Roles
            return customUser;
        }).orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user email in the database for " + email));
    }

    public UserDetails loadUserById(final Long id) {
        final Optional<Users> dbUser = userRepository.findById(id);
        logger.info("Fetched user : " + dbUser + " by " + id);
        return dbUser.map(users -> {
            final CustomUser customUser =  new CustomUser();
            customUser.setId(users.getId());
            customUser.setPassword(users.getPassword());
            customUser.setEmail(users.getEmail());
            customUser.setActive(users.getActive());
            customUser.setGender(users.getGender());
            customUser.setFirstName(users.getFirstName());
            customUser.setLastName(users.getLastName());
            customUser.setIsEmailVerified(users.getIsEmailVerified());
            customUser.setRole(users.getRole());
            //@Todo: Set Roles
            return customUser;
        }).orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user id in the database for " + id));
    }
}
