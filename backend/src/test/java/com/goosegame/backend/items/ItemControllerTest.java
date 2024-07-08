package com.goosegame.backend.items;

import com.goosegame.backend.goose.Goose;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ItemControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void getAllItemsTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("alice", "password1");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Item>> response =
                restTemplate.exchange("/item", HttpMethod.GET, entity,
                        new ParameterizedTypeReference<List<Item>>() {});
        List<Item> resultList = response.getBody();
        resultList.sort(Comparator.comparing(Item::getId));
        assertEquals(2, resultList.size());

        Item aliceItem1 = new Item(1L, "Item1", 10, 20 ,false);
        Item aliceItem2 = new Item(2L, "Item2", 15, 25, true);
        assertEquals(List.of(aliceItem1, aliceItem2), resultList);

        headers.setBasicAuth("bob", "password2");
        entity = new HttpEntity<>(headers);
        response =
                restTemplate.exchange("/item", HttpMethod.GET, entity,
                        new ParameterizedTypeReference<List<Item>>() {});
        resultList = response.getBody();
        assertEquals(1, resultList.size());

        Item bobItem = new Item(3L, "Item3", 20, 30, false);
        assertEquals(List.of(bobItem), resultList);
    }

    @Test
    @DirtiesContext
    void useItemTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("alice", "password1");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Goose> responseGoose
                = restTemplate.exchange("/item/use/1", HttpMethod.POST, entity, Goose.class);

        Goose expectedResult = new Goose(1L, "Goose1", false, 2, 100, 0);
        assertEquals(expectedResult, responseGoose.getBody());

        ResponseEntity<List<Item>> response =
                restTemplate.exchange("/item", HttpMethod.GET, entity,
                        new ParameterizedTypeReference<List<Item>>() {});
        List<Item> resultList = response.getBody();
        resultList.sort(Comparator.comparing(Item::getId));
        assertEquals(1, resultList.size());

        Item aliceItem2 = new Item(2L, "Item2", 15, 25, true);
        assertEquals(List.of(aliceItem2), resultList);

        responseGoose
                = restTemplate.exchange("/item/use/1", HttpMethod.POST, entity, Goose.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseGoose.getStatusCode());

        responseGoose
                = restTemplate.exchange("/item/use/3", HttpMethod.POST, entity, Goose.class);
        assertEquals(HttpStatus.FORBIDDEN, responseGoose.getStatusCode());

    }

    @Test
    @DirtiesContext
    void deleteItemTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("alice", "password1");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response =
                restTemplate.exchange("/item/delete/1", HttpMethod.DELETE, entity, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<List<Item>> itemCheck =
                restTemplate.exchange("/item", HttpMethod.GET, entity,
                        new ParameterizedTypeReference<List<Item>>() {});
        List<Item> resultList = itemCheck.getBody();
        Item aliceItem2 = new Item(2L, "Item2", 15, 25, true);
        assertEquals(List.of(aliceItem2), resultList);

        response =
                restTemplate.exchange("/item/delete/1", HttpMethod.DELETE, entity, Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response =
                restTemplate.exchange("/item/delete/4", HttpMethod.DELETE, entity, Void.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}