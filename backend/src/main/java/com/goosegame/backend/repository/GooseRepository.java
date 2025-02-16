package com.goosegame.backend.repository;

import com.goosegame.backend.model.Goose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GooseRepository extends JpaRepository<Goose, Long> {
    boolean existsByUserUsername(String username);
    Optional<Goose> findGooseByUserUsername(String username);
}
