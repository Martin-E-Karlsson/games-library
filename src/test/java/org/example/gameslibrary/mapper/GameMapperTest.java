package org.example.gameslibrary.mapper;

import org.example.gameslibrary.dto.CreateGameDto;
import org.example.gameslibrary.dto.GameDto;
import org.example.gameslibrary.entity.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameMapperTest {

    private GameMapper gameMapper;

    @BeforeEach
    void setUp() {
        gameMapper = new GameMapper();
    }

    @Test
    void toEntity_shouldMapAllFields() {
        CreateGameDto dto = new CreateGameDto();
        dto.setTitle("Test Game");
        dto.setDescription("A test description");
        dto.setReleaseDate(LocalDate.of(2023, 1, 15));
        dto.setPrice(new BigDecimal("29.99"));
        dto.setCategories(List.of("Action", "RPG"));

        Game game = gameMapper.toEntity(dto);

        assertNull(game.getId());
        assertEquals("Test Game", game.getTitle());
        assertEquals("A test description", game.getDescription());
        assertEquals(LocalDate.of(2023, 1, 15), game.getReleaseDate());
        assertEquals(new BigDecimal("29.99"), game.getPrice());
        assertEquals(List.of("Action", "RPG"), game.getCategories());
    }

    @Test
    void toEntity_shouldHandleNullCategories() {
        CreateGameDto dto = new CreateGameDto();
        dto.setTitle("Test Game");
        dto.setDescription("A test description");
        dto.setReleaseDate(LocalDate.of(2023, 1, 15));
        dto.setPrice(new BigDecimal("29.99"));
        dto.setCategories(null);

        Game game = gameMapper.toEntity(dto);

        assertNotNull(game.getCategories());
        assertTrue(game.getCategories().isEmpty());
    }

    @Test
    void toEntity_shouldCreateDefensiveCopyOfCategories() {
        List<String> categories = new java.util.ArrayList<>(List.of("Action"));
        CreateGameDto dto = new CreateGameDto();
        dto.setTitle("Test Game");
        dto.setDescription("A test description");
        dto.setReleaseDate(LocalDate.of(2023, 1, 15));
        dto.setPrice(new BigDecimal("29.99"));
        dto.setCategories(categories);

        Game game = gameMapper.toEntity(dto);
        categories.add("RPG");

        assertEquals(1, game.getCategories().size());
    }

    @Test
    void toDto_shouldMapAllFields() {
        Game game = new Game();
        game.setId(1L);
        game.setTitle("Test Game");
        game.setDescription("A test description");
        game.setReleaseDate(LocalDate.of(2023, 1, 15));
        game.setPrice(new BigDecimal("29.99"));
        game.setCategories(List.of("Action", "RPG"));

        GameDto dto = gameMapper.toDto(game);

        assertEquals(1L, dto.getId());
        assertEquals("Test Game", dto.getTitle());
        assertEquals("A test description", dto.getDescription());
        assertEquals(LocalDate.of(2023, 1, 15), dto.getReleaseDate());
        assertEquals(new BigDecimal("29.99"), dto.getPrice());
        assertEquals(List.of("Action", "RPG"), dto.getCategories());
    }

    @Test
    void toDto_shouldHandleNullCategories() {
        Game game = new Game();
        game.setId(1L);
        game.setTitle("Test Game");
        game.setDescription("A test description");
        game.setReleaseDate(LocalDate.of(2023, 1, 15));
        game.setPrice(new BigDecimal("29.99"));
        game.setCategories(null);

        GameDto dto = gameMapper.toDto(game);

        assertNotNull(dto.getCategories());
        assertTrue(dto.getCategories().isEmpty());
    }

    @Test
    void toDto_shouldReturnImmutableCategories() {
        Game game = new Game();
        game.setId(1L);
        game.setTitle("Test Game");
        game.setDescription("A test description");
        game.setReleaseDate(LocalDate.of(2023, 1, 15));
        game.setPrice(new BigDecimal("29.99"));
        game.setCategories(List.of("Action"));

        GameDto dto = gameMapper.toDto(game);

        assertThrows(UnsupportedOperationException.class, () -> dto.getCategories().add("RPG"));
    }
}