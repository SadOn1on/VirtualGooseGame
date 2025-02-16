package com.goosegame.backend.service;

import com.goosegame.backend.dto.GooseDto;
import com.goosegame.backend.mapper.MapstructMapper;
import com.goosegame.backend.model.Goose;
import com.goosegame.backend.model.User;
import com.goosegame.backend.repository.GooseRepository;
import com.goosegame.backend.model.Item;
import com.goosegame.backend.repository.ItemRepository;
import com.goosegame.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class GooseService {

    private final GooseRepository gooseRepository;
    private final ItemRepository itemRepository;
    private final GooseAsyncService gooseAsyncService;
    private final UserRepository userRepository;
    private final MapstructMapper mapper;
    private final ItemFactory itemFactory;

    @Autowired
    public GooseService(GooseRepository gooseRepository,
                        ItemRepository itemRepository,
                        GooseAsyncService gooseAsyncService,
                        UserRepository userRepository,
                        MapstructMapper mapper,
                        ItemFactory itemFactory
    ) {
        this.gooseRepository = gooseRepository;
        this.itemRepository = itemRepository;
        this.gooseAsyncService = gooseAsyncService;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.itemFactory = itemFactory;
    }

    public GooseDto serveGoose(String username) {
        // check if user played before
        if (userRepository.findByUsername(username).isEmpty()) {
            Item item = itemFactory.getCommonItem();
            Goose goose = new Goose(username + "_goose");
            User user = User.builder()
                    .username(username)
                    .enabled(true)
                    .goose(goose)
                    .items(List.of(item))
                    .build();
            item.setUser(user);
            goose.setUser(user);
            userRepository.save(user);
        }
        return findByUsername(username);
    }

    public GooseDto findByUsername(String username) {
        return mapper.toGooseDto(gooseRepository.findGooseByUserUsername(username).get());
    }

    public void createNewGooseForUser(String username) {
        if (gooseRepository.existsByUserUsername(username)) {
            gooseRepository.delete(gooseRepository.findGooseByUserUsername(username).get());
        }
        Goose newGoose = new Goose(username, 1, false, 100, 0);
        newGoose.setUser(userRepository.findByUsername(username).get());
        gooseRepository.save(newGoose);
    }

    public GooseDto useItem(String username, Item item) throws ItemDoesNotBelongToUserException {
        if (!itemRepository.findUserUsernameById(item.getId()).equals(username)) {
            throw new ItemDoesNotBelongToUserException("User does not have this item");
        }

        Goose goose = gooseRepository.findGooseByUserUsername(username).get();

        goose.changeHunger(-item.getNutritionValue());
        goose.changeHealth(item.getHealthValue());
        goose.setIsIll(goose.getIsIll() || item.isInfected());

        itemRepository.deleteById(item.getId());

        return mapper.toGooseDto(gooseRepository.save(goose));
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelayString = "${app.goose-update-timing}")
    protected void changeGeeseState() {
        Page<Goose> geese = gooseRepository.findAll(PageRequest.ofSize(100));
        List<Goose> geeseList = geese.getContent();
        gooseAsyncService.workWithGeese(geeseList);

        while (geese.hasNext()) {
            geese = gooseRepository.findAll(geese.nextPageable());
            geeseList = geese.getContent();
            gooseAsyncService.workWithGeese(geeseList);
        }
    }
}
