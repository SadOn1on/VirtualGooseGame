package com.goosegame.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authz -> authz
                        .requestMatchers("item/**").hasRole("USER")
                        .requestMatchers("/user/login").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("user/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "goose").hasAnyRole("USER", "ADMIN")
                        .anyRequest().permitAll())
                .httpBasic(withDefaults())
                .csrf(CsrfConfigurer::disable);
        return http.build();
    }

    @Bean
    JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?");
        users.setAuthoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?");
        users.setCreateUserSql("INSERT INTO users (username, password, enabled) VALUES (?, ?, ?)");
        users.setUpdateUserSql("UPDATE users SET password = ?, enabled = ? WHERE username = ?");
        users.setDeleteUserSql("DELETE FROM users WHERE username = ?");
        users.setCreateAuthoritySql("INSERT INTO authorities (username, authority) VALUES (?, ?)");
        users.setDeleteUserAuthoritiesSql("DELETE FROM authorities WHERE username = ?");

        return users;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
