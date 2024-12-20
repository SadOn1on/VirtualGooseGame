package com.goosegame.backend.repository;

import com.goosegame.backend.model.Goose;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GooseRepository extends JpaRepository<Goose, Long> {
    boolean existsByUserUsername(String username);
    Goose findGooseByUserUsername(String username);
}
