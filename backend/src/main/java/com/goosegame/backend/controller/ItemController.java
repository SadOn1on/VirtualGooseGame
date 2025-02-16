package com.goosegame.backend.controller;

import com.goosegame.backend.dto.GooseDto;
import com.goosegame.backend.dto.ItemDto;
import com.goosegame.backend.service.GooseService;
import com.goosegame.backend.service.ItemService;
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
    public List<ItemDto> getAllItems(Principal principal) {
        return itemService.getAllByUsername(principal.getName());
    }

    @PostMapping("/use/{itemId}")
    public GooseDto useItem(@PathVariable Long itemId, Principal principal) {
        return gooseService.useItem(principal.getName(), itemService.getById(itemId));
    }

    @DeleteMapping("/delete/{itemId}")
    public void deleteItem(@PathVariable Long itemId, Principal principal) {
        itemService.deleteById(itemId, principal.getName());
    }

}
