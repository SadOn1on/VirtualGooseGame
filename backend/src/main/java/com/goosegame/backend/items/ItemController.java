package com.goosegame.backend.items;

import com.goosegame.backend.goose.Goose;
import com.goosegame.backend.goose.GooseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final GooseService gooseService;
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService, GooseService gooseService) {
        this.itemService = itemService;
        this.gooseService = gooseService;
    }

    @GetMapping
    public List<Item> getAllItems(Principal principal) {
        return itemService.getAllByUsername(principal.getName());
    }

    @PostMapping("/use/{itemId}")
    public Goose useItem(@PathVariable Long itemId, Principal principal) {
        return gooseService.useItem(principal.getName(), itemService.getById(itemId));
    }

    @DeleteMapping("/delete/{itemId}")
    public void deleteItem(@PathVariable Long itemId, Principal principal) {
        itemService.deleteById(itemId, principal.getName());
    }

}
