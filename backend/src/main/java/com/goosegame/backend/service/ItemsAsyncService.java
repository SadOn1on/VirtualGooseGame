package com.goosegame.backend.service;

import com.goosegame.backend.model.Item;
import com.goosegame.backend.repository.ItemRepository;
import com.goosegame.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemsAsyncService {

    private final ItemRepository itemRepository;
    private final ItemFactory itemFactory;

    @Autowired
    public ItemsAsyncService(ItemRepository itemRepository, ItemFactory itemFactory) {
        this.itemRepository = itemRepository;
        this.itemFactory = itemFactory;
    }

    @Async
    public void createNewItems(List<User> users) {
        for (User u : users) {
            Item item = itemFactory.getRandomItem();
            item.setUser(u);
            itemRepository.save(item);
        }
    }

}
