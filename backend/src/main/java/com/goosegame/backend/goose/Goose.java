package com.goosegame.backend.goose;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goosegame.backend.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "t_goose")
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

    public Integer getHunger() {
        return hunger;
    }

    public void setHunger(Integer hunger) {
        this.hunger = hunger;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getIll() {
        return isIll;
    }

    public void setIll(Boolean ill) {
        isIll = ill;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
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
