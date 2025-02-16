package com.goosegame.backend.service;

import com.goosegame.backend.dto.ItemDto;
import com.goosegame.backend.mapper.MapstructMapper;
import com.goosegame.backend.model.Item;
import com.goosegame.backend.repository.ItemRepository;
import com.goosegame.backend.model.User;
import com.goosegame.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemsAsyncService itemsAsyncService;
    private final UserRepository userRepository;
    private final MapstructMapper mapper;

    @Autowired
    public ItemService(
            ItemRepository itemRepository,
            ItemsAsyncService itemsAsyncService,
            UserRepository userRepository,
            MapstructMapper mapper
    ) {
        this.itemRepository = itemRepository;
        this.itemsAsyncService = itemsAsyncService;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<ItemDto> getAllByUsername(String username) {
        return itemRepository.findAllByUserUsername(username).stream()
                .map(mapper::toItemDto)
                .toList();
    }

    public void deleteById(Long id, String username) throws ItemDoesNotBelongToUserException, ItemDoesNotExistException {
        if (!itemRepository.existsById(id)) {
            throw new ItemDoesNotExistException("Item does not exist");
        }
        if (!belongsToUser(id, username)) {
            throw new ItemDoesNotBelongToUserException("User does not have this item");
        }
        itemRepository.deleteById(id);
    }

    public Item getById(Long id) throws ItemDoesNotExistException {
        if (!itemRepository.existsById(id)) {
            throw new ItemDoesNotExistException("Item does not exist");
        }
        return itemRepository.getReferenceById(id);
    }

    public boolean belongsToUser(Long id, String username) {
        return itemRepository.findUserUsernameById(id).equals(username);
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelayString = "${app.item-add-timing}")
    protected void giveRandomItemsToUsers() {
        Page<User> users = userRepository.findAll(PageRequest.ofSize(100));
        List<User> userList = users.getContent();
        itemsAsyncService.createNewItems(userList);

        while (users.hasNext()) {
            users = userRepository.findAll(users.nextPageable());
            userList = users.getContent();
            itemsAsyncService.createNewItems(userList);
        }
    }
}
