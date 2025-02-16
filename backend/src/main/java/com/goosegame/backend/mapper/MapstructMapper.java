package com.goosegame.backend.mapper;

import com.goosegame.backend.configuration.MapstructConfiguration;
import com.goosegame.backend.dto.GooseDto;
import com.goosegame.backend.dto.ItemDto;
import com.goosegame.backend.model.Goose;
import com.goosegame.backend.model.Item;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfiguration.class)
public interface MapstructMapper {

    GooseDto toGooseDto(Goose goose);

    ItemDto toItemDto(Item item);

}
