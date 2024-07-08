package com.goosegame.backend.goose;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GooseRepository extends JpaRepository<Goose, Long> {
    boolean existsByUserUsername(String username);
    Goose findGooseByUserUsername(String username);
}
