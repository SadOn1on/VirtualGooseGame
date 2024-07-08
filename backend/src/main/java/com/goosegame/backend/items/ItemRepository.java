package com.goosegame.backend.items;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByUserUsername(String username);
    @Query("SELECT i.user.username FROM Item i WHERE i.id = :id")
    String findUserUsernameById(@Param("id") Long id);
}
