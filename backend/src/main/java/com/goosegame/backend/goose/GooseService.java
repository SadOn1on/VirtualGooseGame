package com.goosegame.backend.goose;

import com.goosegame.backend.items.Item;
import com.goosegame.backend.items.ItemDoesNotBelongToUserException;
import com.goosegame.backend.items.ItemRepository;
import com.goosegame.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class GooseService {

    private final GooseRepository gooseRepository;
    private final ItemRepository itemRepository;
    private final GooseAsyncService gooseAsyncService;
    private final UserRepository userRepository;

    @Autowired
    public GooseService(GooseRepository gooseRepository,
                        ItemRepository itemRepository,
                        GooseAsyncService gooseAsyncService,
                        UserRepository userRepository) {
        this.gooseRepository = gooseRepository;
        this.itemRepository = itemRepository;
        this.gooseAsyncService = gooseAsyncService;
        this.userRepository = userRepository;
    }

    public Goose findByUsername(String username) {
        return gooseRepository.findGooseByUserUsername(username);
    }

    public void createNewGooseForUser(String username) {
        if (gooseRepository.existsByUserUsername(username)) {
            gooseRepository.deleteById(findByUsername(username).getId());
        }
        Goose newGoose = new Goose(username, 1, false, 100, 0);
        newGoose.setUser(userRepository.findByUsername(username));
        gooseRepository.save(newGoose);
    }

    public Goose useItem(String username, Item item) throws ItemDoesNotBelongToUserException {
        if (!itemRepository.findUserUsernameById(item.getId()).equals(username)) {
            throw new ItemDoesNotBelongToUserException("User does not have this item");
        }

        Goose goose = gooseRepository.findGooseByUserUsername(username);

        goose.changeHunger(-item.getNutritionValue());
        goose.changeHealth(item.getHealthValue());
        goose.setIll(goose.getIll() || item.isInfected());

        itemRepository.deleteById(item.getId());

        return gooseRepository.save(goose);
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
