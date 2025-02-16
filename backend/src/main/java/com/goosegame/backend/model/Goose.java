package com.goosegame.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "goose")
@Getter
@Setter
public class Goose {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_goose_seq")
    @SequenceGenerator(name = "t_goose_seq", sequenceName = "t_goose_seq", allocationSize = 1)
    private Long id;

    @Column(name = "goose_name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "is_ill")
    private Boolean isIll;

    @Column(name = "health")
    private Integer health;

    @Column(name = "hunger")
    private Integer hunger;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    public Goose() {
    }

    public Goose(String name) {
        this.name = name;
        age = 0;
        isIll = false;
        health = 100;
        hunger = 0;
    }

    public Goose(String name, Integer age, Boolean isIll, Integer health, Integer hunger) {
        this.name = name;
        this.age = age;
        this.isIll = isIll;
        this.health = health;
        this.hunger = hunger;
    }

    public Goose(Long id, String name, Boolean isIll, Integer age, Integer health, Integer hunger) {
        this.id = id;
        this.name = name;
        this.isIll = isIll;
        this.age = age;
        this.health = health;
        this.hunger = hunger;
    }

    public void changeHunger(int step) {
        if (hunger + step > 100) {
            hunger = 100;
        } else if (hunger + step < 0) {
            hunger = 0;
        } else {
            hunger += step;
        }
    }

    public void changeHealth(int step) {
        if (health + step > 100) {
            health = 100;
        } else if (health + step < 0) {
            health = 0;
        } else {
            health += step;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Goose goose = (Goose) o;
        return id.equals(goose.id) && name.equals(goose.name) && age.equals(goose.age) && isIll.equals(goose.isIll) && health.equals(goose.health) && hunger.equals(goose.hunger);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + age.hashCode();
        result = 31 * result + isIll.hashCode();
        result = 31 * result + health.hashCode();
        result = 31 * result + hunger.hashCode();
        return result;
    }
}
