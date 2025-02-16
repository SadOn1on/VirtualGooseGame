package com.goosegame.backend.dto;

public record ItemDto(
        Long id,
        String name,
        int healthValue,
        int nutritionValue,
        boolean infected
) {
}
