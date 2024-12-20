package com.goosegame.backend.controller;

import com.goosegame.backend.service.GooseService;
import com.goosegame.backend.service.AuthenticationService;
import com.goosegame.backend.model.AuthorityResponse;
import com.goosegame.backend.model.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final GooseService gooseService;

    @Autowired
    public UserController(AuthenticationService authenticationService, PasswordEncoder passwordEncoder, GooseService gooseService) {
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
        this.gooseService = gooseService;
    }

    @PostMapping("/register")
    public UserRegistrationRequest registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserDetails user = User.builder()
                .username(userRegistrationRequest.getUsername())
                .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .roles("USER")
                .build();

        authenticationService.registerUser(user);
        gooseService.createNewGooseForUser(user.getUsername());

        return userRegistrationRequest;
    }

    @PostMapping("/admin")
    public UserRegistrationRequest registerAdmin(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserDetails user = User.builder()
                .username(userRegistrationRequest.getUsername())
                .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .roles("ADMIN")
                .build();

        authenticationService.registerUser(user);
        gooseService.createNewGooseForUser(user.getUsername());

        return userRegistrationRequest;
    }

    @GetMapping("/login")
    public AuthorityResponse userLoginCheck(Principal principal) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            authorities.add(grantedAuthority.toString());
        }
        return new AuthorityResponse(principal.getName(), authorities);
    }

}
