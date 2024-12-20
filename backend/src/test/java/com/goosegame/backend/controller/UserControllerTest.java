package com.goosegame.backend.controller;

import com.goosegame.backend.model.Goose;
import com.goosegame.backend.model.AuthorityResponse;
import com.goosegame.backend.model.UserRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void loginCheck() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("alice", "password1");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<AuthorityResponse> response =
                restTemplate.exchange("/user/login", HttpMethod.GET, entity, AuthorityResponse.class);
        AuthorityResponse expectedAuthorities =
                new AuthorityResponse("alice", Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
        assertEquals(expectedAuthorities, response.getBody());

        headers.setBasicAuth("bob", "password2");
        entity = new HttpEntity<>(headers);
        response = restTemplate.exchange("/user/login", HttpMethod.GET, entity, AuthorityResponse.class);
        expectedAuthorities.setAuthorities(List.of("ROLE_USER"));
        expectedAuthorities.setUsername("bob");
        assertEquals(expectedAuthorities, response.getBody());

        headers.clear();
        entity = new HttpEntity<>(headers);
        response = restTemplate.exchange("/user/login", HttpMethod.GET, entity, AuthorityResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        headers.setBasicAuth("carol", "password1");
        entity = new HttpEntity<>(headers);
        response = restTemplate.exchange("/user/login", HttpMethod.GET, entity, AuthorityResponse.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    void registerUserCheck() {
        UserRegistrationRequest registrationRequest =
                new UserRegistrationRequest("testUser", "testPassword");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserRegistrationRequest> entity = new HttpEntity<>(registrationRequest, headers);
        ResponseEntity<UserRegistrationRequest> response =
                restTemplate.exchange("/user/register", HttpMethod.POST, entity, UserRegistrationRequest.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(registrationRequest, response.getBody());

        headers.setBasicAuth("testUser", "testPassword");
        HttpEntity<String> loginEntity = new HttpEntity<>(headers);
        ResponseEntity<AuthorityResponse> authorityResponseResponseEntityResponse
                = restTemplate.exchange("/user/login", HttpMethod.GET, loginEntity, AuthorityResponse.class);
        AuthorityResponse expectedAuthorities = new AuthorityResponse("testUser", List.of("ROLE_USER"));
        assertEquals(expectedAuthorities, authorityResponseResponseEntityResponse.getBody());

        //check if goose was created for new user
        ResponseEntity<Goose> newlyCreatedGoose =
                restTemplate.exchange("/goose", HttpMethod.GET, loginEntity, Goose.class);
        assertEquals(HttpStatus.OK, newlyCreatedGoose.getStatusCode());
        assertEquals("testUser", newlyCreatedGoose.getBody().getName());

        headers.clear();
        entity = new HttpEntity<>(registrationRequest, headers);
        response = restTemplate.exchange("/user/register", HttpMethod.POST, entity, UserRegistrationRequest.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    void registerAdminCheck() {
        UserRegistrationRequest registrationRequest =
                new UserRegistrationRequest("testUser", "testPassword");

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("alice", "password1");
        HttpEntity<UserRegistrationRequest> entity = new HttpEntity<>(registrationRequest, headers);
        ResponseEntity<UserRegistrationRequest> response =
                restTemplate.exchange("/user/admin", HttpMethod.POST, entity, UserRegistrationRequest.class);
        assertEquals(registrationRequest, response.getBody());

        headers.setBasicAuth("testUser", "testPassword");
        HttpEntity<String> loginEntity = new HttpEntity<>(headers);
        ResponseEntity<AuthorityResponse> authorityResponseResponseEntityResponse
                = restTemplate.exchange("/user/login", HttpMethod.GET, loginEntity, AuthorityResponse.class);
        AuthorityResponse expectedAuthorities = new AuthorityResponse("testUser", List.of("ROLE_ADMIN"));
        assertEquals(expectedAuthorities, authorityResponseResponseEntityResponse.getBody());

        //check if goose was created for new user
        ResponseEntity<Goose> newlyCreatedGoose =
                restTemplate.exchange("/goose", HttpMethod.GET, loginEntity, Goose.class);
        assertEquals(HttpStatus.OK, newlyCreatedGoose.getStatusCode());
        assertEquals("testUser", newlyCreatedGoose.getBody().getName());

        headers.setBasicAuth("alice", "password1");
        entity = new HttpEntity<>(registrationRequest, headers);
        response = restTemplate.exchange("/user/admin", HttpMethod.POST, entity, UserRegistrationRequest.class);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        headers.clear();
        entity = new HttpEntity<>(registrationRequest, headers);
        response = restTemplate.exchange("/user/admin", HttpMethod.POST, entity, UserRegistrationRequest.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        headers.setBasicAuth("bob", "password2");
        entity = new HttpEntity<>(registrationRequest, headers);
        response = restTemplate.exchange("/user/admin", HttpMethod.POST, entity, UserRegistrationRequest.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}