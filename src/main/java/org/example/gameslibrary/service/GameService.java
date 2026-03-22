package org.example.gameslibrary.service;

import org.example.gameslibrary.dto.CreateGameDto;
import org.example.gameslibrary.dto.GameDto;
import org.example.gameslibrary.dto.UpdateGameDto;
import org.example.gameslibrary.entity.Game;
import org.example.gameslibrary.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<GameDto> findAll() {
        return gameRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public GameDto findById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + id)); //Placeholder until ResourceNotFoundException is implemented
        return toDto(game);
    }

    public GameDto create(CreateGameDto createDto) {
        validateTitle(createDto.getTitle(), null);
        validatePrice(createDto.getPrice());
        validateCategories(createDto.getCategories());

        Game game = toEntity(createDto);
        Game saved = gameRepository.save(game);
        return toDto(saved);
    }

    public GameDto update(UpdateGameDto updateDto) {
        Game foundGame = gameRepository.findById(updateDto.getId())
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + updateDto.getId())); //Placeholder until ResourceNotFoundException is implemented

        if (updateDto.getTitle() != null) {
            validateTitle(updateDto.getTitle(), foundGame.getId());
            foundGame.setTitle(updateDto.getTitle());
        }
        if (updateDto.getDescription() != null) {
            foundGame.setDescription(updateDto.getDescription());
        }
        if (updateDto.getReleaseDate() != null) {
            foundGame.setReleaseDate(updateDto.getReleaseDate());

        }
        if (updateDto.getPrice() != null) {
            validatePrice(updateDto.getPrice());
            foundGame.setPrice(updateDto.getPrice());
        }
        if (updateDto.getCategories() != null) {
            validateCategories(updateDto.getCategories());
            foundGame.setCategories(updateDto.getCategories());
        }

        Game saved = gameRepository.save(foundGame);
        return toDto(saved);
    }

    public void deleteById(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new RuntimeException("Game not found with id: " + id); // Placeholder until ResourceNotFoundException is implemented
        }
        gameRepository.deleteById(id);
    }

    private void validateTitle(String title, Long excludeId) {
        gameRepository.findAll().stream()
                .filter(game -> game.getTitle().equalsIgnoreCase(title))
                .filter(game -> !game.getId().equals(excludeId))
                .findFirst()
                .ifPresent(game -> {
                    throw new RuntimeException("A game with the title '" + title + "' already exists");
                });
    }

    private void validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Price must be positive"); // Placeholder until ResourceNotFoundException is implemented
        }
    }

    private void validateCategories(List<String> categories) {
        if (categories == null) {
            return;
        }
        long uniqueCount = categories.stream().map(String::toLowerCase).distinct().count();
        if (uniqueCount != categories.size()) {
            throw new RuntimeException("Categories must not contain duplicates"); // Placeholder until ResourceNotFoundException is implemented
        }
    }

    private GameDto toDto(Game game) {
        return null; // Mapper will replace this
    }

    private Game toEntity(CreateGameDto dto) {
        return null; // Mapper will replace this
    }
}