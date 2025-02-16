package by.zharski.SSO.service;

import by.zharski.SSO.dto.RegistrationRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final JdbcUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(JdbcUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegistrationRequest registrationRequest) throws UserExistsException {
        if (userDetailsManager.userExists(registrationRequest.username())) {
            throw new UserExistsException("User with that username already exists");
        }

        UserDetails user = User.builder()
                .username(registrationRequest.username())
                .password(passwordEncoder.encode(registrationRequest.password()))
                .roles("USER")
                .build();

        userDetailsManager.createUser(user);
    }

}
