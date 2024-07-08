package com.goosegame.backend.goose;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GooseControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getGooseTest() {
        Goose expectedGoose = new Goose(1L, "Goose1", false, 2, 100, 20);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("alice", "password1");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Goose> response = restTemplate.exchange("/goose", HttpMethod.GET, entity, Goose.class);
        Goose responseGoose = response.getBody();
        assertEquals(expectedGoose, responseGoose);

        HttpEntity<String> noAuthEntity = new HttpEntity<>(new HttpHeaders());
        response = restTemplate.exchange("/goose", HttpMethod.GET, noAuthEntity, Goose.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

}