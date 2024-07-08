package com.goosegame.backend.items;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ItemFactory {

    private final List<Item> commonItems;
    private final List<Item> rareItems;
    private final List<Item> epicItems;

    public ItemFactory(List<Item> commonItems, List<Item> rareItems, List<Item> epicItems) {
        this.commonItems = commonItems;
        this.rareItems = rareItems;
        this.epicItems = epicItems;
    }

    public Item getCommonItem() {
        return new Item(commonItems.get(ThreadLocalRandom.current().nextInt(commonItems.size())));
    }

    public Item getRareItem() {
        return new Item(rareItems.get(ThreadLocalRandom.current().nextInt(rareItems.size())));
    }

    public Item getEpicItem() {
        return new Item(epicItems.get(ThreadLocalRandom.current().nextInt(epicItems.size())));
    }

    public Item getRandomItem() {
        int choice = ThreadLocalRandom.current().nextInt(3);

        if (choice == 0) {
            return getCommonItem();
        } else if (choice == 1) {
            return getRareItem();
        } else {
            return getEpicItem();
        }
    }

}
