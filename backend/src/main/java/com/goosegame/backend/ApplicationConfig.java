package com.goosegame.backend;

import com.goosegame.backend.items.Item;
import com.goosegame.backend.items.ItemFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public ItemFactory itemFactory() {
        List<Item> commonItems = new ArrayList<>();
        List<Item> rareItems = new ArrayList<>();
        List<Item> epicItems = new ArrayList<>();

        commonItems.add(new Item("Seeds", 5, 10, false));
        commonItems.add(new Item("Worm", 3, 15, false));
        commonItems.add(new Item("Berry snack", 8, 12, false));
        commonItems.add(new Item("Water", 0, 5, false));
        commonItems.add(new Item("White bread", 0, 20, true));

        rareItems.add(new Item("Healing Herb", 30, 0, false));
        rareItems.add(new Item("Vitality Elixir", 50, 20, false));

        epicItems.add(new Item("Golden Grain", 100, 50, false));
        epicItems.add(new Item("Magic Elixir", 80, 30, false));

        return new ItemFactory(commonItems, rareItems, epicItems);
    }
}
