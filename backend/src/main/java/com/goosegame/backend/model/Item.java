package com.goosegame.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item")
@Getter
@Setter
public class Item {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_item_seq")
    @SequenceGenerator(name = "t_item_seq", sequenceName = "t_item_seq", allocationSize = 1)
    Long id;

    @Column(name = "item_name")
    String name;

    @Column(name = "health_value")
    int healthValue;

    @Column(name = "nutrition_value")
    int nutritionValue;

    @Column(name = "is_infected")
    boolean infected;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    public Item() {}

    public Item(Long id, String name, int healthValue, int nutritionValue, boolean infected) {
        this.id = id;
        this.name = name;
        this.healthValue = healthValue;
        this.nutritionValue = nutritionValue;
        this.infected = infected;
    }

    public Item(String name, int healthValue, int nutritionValue, boolean infected) {
        this.name = name;
        this.healthValue = healthValue;
        this.nutritionValue = nutritionValue;
        this.infected = infected;
    }

    public Item(Item item) {
        this.name = item.getName();
        this.healthValue = item.getHealthValue();
        this.nutritionValue = item.getNutritionValue();
        this.infected = item.isInfected();
    }

}
