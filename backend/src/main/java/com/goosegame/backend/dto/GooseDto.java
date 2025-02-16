package com.goosegame.backend.dto;

public record GooseDto(
        String name,
        Integer age,
        Boolean isIll,
        Integer health,
        Integer hunger
) {
}
