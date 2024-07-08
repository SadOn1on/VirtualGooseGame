package com.goosegame.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    public AuthenticationService(JdbcUserDetailsManager jdbcUserDetailsManager) {
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
    }


    public void registerUser(UserDetails userDetails) throws UserAlreadyExistsException {
        if (jdbcUserDetailsManager.userExists(userDetails.getUsername())) {
            throw new UserAlreadyExistsException("User with that username already exists");
        }

        jdbcUserDetailsManager.createUser(userDetails);
    }
}
