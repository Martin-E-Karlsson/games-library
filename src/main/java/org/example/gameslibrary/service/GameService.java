package org.example.gameslibrary.service;

import org.example.gameslibrary.dto.CreateGameDto;
import org.example.gameslibrary.dto.GameDto;
import org.example.gameslibrary.dto.UpdateGameDto;
import org.example.gameslibrary.entity.Game;
import org.example.gameslibrary.mapper.GameMapper;
import org.example.gameslibrary.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.example.gameslibrary.exception.ResourceNotFoundException;
import org.example.gameslibrary.exception.DuplicateTitleException;
import org.example.gameslibrary.exception.InvalidPriceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    public GameService(GameRepository gameRepository, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
    }

    public Page<GameDto> findAll(String title, String category, Pageable pageable) {
        Page<Game> games;

        boolean hasTitle = title != null && !title.isBlank();
        boolean hasCategory = category != null && !category.isBlank();

        if (hasTitle && hasCategory) {
            games = gameRepository.findByTitleContainingIgnoreCaseAndCategory(title.trim(), category.trim(), pageable);
        } else if (hasTitle) {
            games = gameRepository.findByTitleContainingIgnoreCase(title.trim(), pageable);
        } else if (hasCategory) {
            games = gameRepository.findByCategory(category.trim(), pageable);
        } else {
            games = gameRepository.findAll(pageable);
        }

        return games.map(gameMapper::toDto);
    }

    public GameDto findById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + id));
        return gameMapper.toDto(game);
    }

    public GameDto create(CreateGameDto createDto) {
        validateTitle(createDto.getTitle(), null);
        validatePrice(createDto.getPrice());

        Game game = gameMapper.toEntity(createDto);
        game.setCategories(deduplicateCategories(createDto.getCategories()));

        Game saved = gameRepository.save(game);
        return gameMapper.toDto(saved);
    }

    public GameDto update(UpdateGameDto updateDto) {
        Game foundGame = gameRepository.findById(updateDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + updateDto.getId()));

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
            foundGame.setCategories(mergeCategories(foundGame.getCategories(), updateDto.getCategories()));
        }

        Game saved = gameRepository.save(foundGame);
        return gameMapper.toDto(saved);
    }

    public void deleteById(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Game not found with id: " + id);
        }
        gameRepository.deleteById(id);
    }

    private void validateTitle(String title, Long excludeId) {
        gameRepository.findAll().stream()
                .filter(game -> game.getTitle().equalsIgnoreCase(title))
                .filter(game -> !game.getId().equals(excludeId))
                .findFirst()
                .ifPresent(game -> {
                    throw new DuplicateTitleException("A game with the title '" + title + "' already exists");
                });
    }

    private void validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException("Price must be positive");
        }
    }

    private List<String> deduplicateCategories(List<String> categories) {
        if (categories == null) {
            return new ArrayList<>();
        }
        List<String> unique = new ArrayList<>();
        for (String category : categories) {
            if (category != null && !category.isBlank()
                    && unique.stream().noneMatch(c -> c.equalsIgnoreCase(category))) {
                unique.add(category.trim());
            }
        }
        return unique;
    }

    private List<String> mergeCategories(List<String> existing, List<String> incoming) {
        List<String> merged = new ArrayList<>(existing);
        for (String category : incoming) {
            if (category != null && !category.isBlank()
                    && merged.stream().noneMatch(c -> c.equalsIgnoreCase(category.trim()))) {
                merged.add(category.trim());
            }
        }
        return merged;
    }
}