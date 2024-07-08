package com.goosegame.backend.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goosegame.backend.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "t_item")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealthValue() {
        return healthValue;
    }

    public void setHealthValue(int healthValue) {
        this.healthValue = healthValue;
    }

    public int getNutritionValue() {
        return nutritionValue;
    }

    public void setNutritionValue(int nutritionValue) {
        this.nutritionValue = nutritionValue;
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    public String getHolderName() {
        return user.getUsername();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;
        return healthValue == item.healthValue && nutritionValue == item.nutritionValue && infected == item.infected && id.equals(item.id) && name.equals(item.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + healthValue;
        result = 31 * result + nutritionValue;
        result = 31 * result + Boolean.hashCode(infected);
        return result;
    }
}
