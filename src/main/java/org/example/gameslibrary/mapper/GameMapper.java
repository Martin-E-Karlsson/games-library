package org.example.gameslibrary.mapper;

import org.example.gameslibrary.dto.CreateGameDto;
import org.example.gameslibrary.dto.GameDto;
import org.example.gameslibrary.entity.Game;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameMapper {

    public Game toEntity(CreateGameDto dto) {
        Game game = new Game();
        game.setTitle(dto.getTitle());
        game.setDescription(dto.getDescription());
        game.setReleaseDate(dto.getReleaseDate());
        game.setPrice(dto.getPrice());
        game.setCategories(dto.getCategories() != null ? new ArrayList<>(dto.getCategories()) : new ArrayList<>());
        return game;
    }

    public GameDto toDto(Game game) {
        return new GameDto(
                game.getId(),
                game.getTitle(),
                game.getDescription(),
                game.getReleaseDate(),
                game.getPrice(),
                game.getCategories() != null ? List.copyOf(game.getCategories()) : List.of()
        );
    }
}