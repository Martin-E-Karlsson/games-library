package org.example.gameslibrary.service;

import org.example.gameslibrary.dto.CreateGameDto;
import org.example.gameslibrary.dto.GameDto;
import org.example.gameslibrary.dto.UpdateGameDto;
import org.example.gameslibrary.entity.Game;
import org.example.gameslibrary.exception.DuplicateTitleException;
import org.example.gameslibrary.exception.InvalidPriceException;
import org.example.gameslibrary.exception.ResourceNotFoundException;
import org.example.gameslibrary.mapper.GameMapper;
import org.example.gameslibrary.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GameService gameService;

    private Game sampleGame;
    private GameDto sampleGameDto;

    @BeforeEach
    void setUp() {
        sampleGame = new Game();
        sampleGame.setId(1L);
        sampleGame.setTitle("Test Game");
        sampleGame.setDescription("A test description");
        sampleGame.setReleaseDate(LocalDate.of(2023, 1, 15));
        sampleGame.setPrice(new BigDecimal("29.99"));
        sampleGame.setCategories(new ArrayList<>(List.of("Action")));

        sampleGameDto = new GameDto(1L, "Test Game", "A test description",
                LocalDate.of(2023, 1, 15), new BigDecimal("29.99"), List.of("Action"));
    }

    // findById tests

    @Test
    void findById_shouldReturnGameDto() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(sampleGame));
        when(gameMapper.toDto(sampleGame)).thenReturn(sampleGameDto);

        GameDto result = gameService.findById(1L);

        assertEquals(sampleGameDto, result);
        verify(gameRepository).findById(1L);
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gameService.findById(99L));
    }

    // create tests

    @Test
    void create_shouldSaveAndReturnGameDto() {
        CreateGameDto createDto = new CreateGameDto();
        createDto.setTitle("New Game");
        createDto.setDescription("New description");
        createDto.setReleaseDate(LocalDate.of(2023, 6, 1));
        createDto.setPrice(new BigDecimal("19.99"));
        createDto.setCategories(List.of("RPG"));

        when(gameRepository.findAll()).thenReturn(List.of());
        when(gameMapper.toEntity(createDto)).thenReturn(sampleGame);
        when(gameRepository.save(any(Game.class))).thenReturn(sampleGame);
        when(gameMapper.toDto(sampleGame)).thenReturn(sampleGameDto);

        GameDto result = gameService.create(createDto);

        assertNotNull(result);
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void create_shouldThrowOnDuplicateTitle() {
        CreateGameDto createDto = new CreateGameDto();
        createDto.setTitle("Test Game");
        createDto.setDescription("Another description");
        createDto.setReleaseDate(LocalDate.of(2023, 6, 1));
        createDto.setPrice(new BigDecimal("19.99"));

        when(gameRepository.findAll()).thenReturn(List.of(sampleGame));

        assertThrows(DuplicateTitleException.class, () -> gameService.create(createDto));
        verify(gameRepository, never()).save(any());
    }

    @Test
    void create_shouldThrowOnDuplicateTitleCaseInsensitive() {
        CreateGameDto createDto = new CreateGameDto();
        createDto.setTitle("test game");
        createDto.setDescription("Another description");
        createDto.setReleaseDate(LocalDate.of(2023, 6, 1));
        createDto.setPrice(new BigDecimal("19.99"));

        when(gameRepository.findAll()).thenReturn(List.of(sampleGame));

        assertThrows(DuplicateTitleException.class, () -> gameService.create(createDto));
    }

    @Test
    void create_shouldThrowOnZeroPrice() {
        CreateGameDto createDto = new CreateGameDto();
        createDto.setTitle("Unique Title");
        createDto.setDescription("Description");
        createDto.setReleaseDate(LocalDate.of(2023, 6, 1));
        createDto.setPrice(BigDecimal.ZERO);

        when(gameRepository.findAll()).thenReturn(List.of());

        assertThrows(InvalidPriceException.class, () -> gameService.create(createDto));
    }

    @Test
    void create_shouldThrowOnNegativePrice() {
        CreateGameDto createDto = new CreateGameDto();
        createDto.setTitle("Unique Title");
        createDto.setDescription("Description");
        createDto.setReleaseDate(LocalDate.of(2023, 6, 1));
        createDto.setPrice(new BigDecimal("-5.00"));

        when(gameRepository.findAll()).thenReturn(List.of());

        assertThrows(InvalidPriceException.class, () -> gameService.create(createDto));
    }

    @Test
    void create_shouldDeduplicateCategories() {
        CreateGameDto createDto = new CreateGameDto();
        createDto.setTitle("Unique Game");
        createDto.setDescription("Description");
        createDto.setReleaseDate(LocalDate.of(2023, 6, 1));
        createDto.setPrice(new BigDecimal("19.99"));
        createDto.setCategories(List.of("Action", "action", "RPG"));

        when(gameRepository.findAll()).thenReturn(List.of());
        when(gameMapper.toEntity(createDto)).thenReturn(new Game());
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(gameMapper.toDto(any(Game.class))).thenReturn(sampleGameDto);

        gameService.create(createDto);

        verify(gameRepository).save(argThat(game ->
                game.getCategories().size() == 2
        ));
    }

    @Test
    void create_shouldFilterBlankCategories() {
        CreateGameDto createDto = new CreateGameDto();
        createDto.setTitle("Unique Game");
        createDto.setDescription("Description");
        createDto.setReleaseDate(LocalDate.of(2023, 6, 1));
        createDto.setPrice(new BigDecimal("19.99"));
        createDto.setCategories(List.of("Action", "", "  "));

        when(gameRepository.findAll()).thenReturn(List.of());
        when(gameMapper.toEntity(createDto)).thenReturn(new Game());
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(gameMapper.toDto(any(Game.class))).thenReturn(sampleGameDto);

        gameService.create(createDto);

        verify(gameRepository).save(argThat(game ->
                game.getCategories().size() == 1
        ));
    }

    // update tests

    @Test
    void update_shouldOnlyUpdateProvidedFields() {
        UpdateGameDto updateDto = new UpdateGameDto();
        updateDto.setId(1L);
        updateDto.setTitle("Updated Title");

        when(gameRepository.findById(1L)).thenReturn(Optional.of(sampleGame));
        when(gameRepository.findAll()).thenReturn(List.of(sampleGame));
        when(gameRepository.save(any(Game.class))).thenReturn(sampleGame);
        when(gameMapper.toDto(sampleGame)).thenReturn(sampleGameDto);

        gameService.update(updateDto);

        assertEquals("Updated Title", sampleGame.getTitle());
        assertEquals("A test description", sampleGame.getDescription());
        assertEquals(new BigDecimal("29.99"), sampleGame.getPrice());
    }

    @Test
    void update_shouldThrowWhenNotFound() {
        UpdateGameDto updateDto = new UpdateGameDto();
        updateDto.setId(99L);

        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gameService.update(updateDto));
    }

    @Test
    void update_shouldMergeCategories() {
        UpdateGameDto updateDto = new UpdateGameDto();
        updateDto.setId(1L);
        updateDto.setCategories(List.of("RPG", "Action"));

        when(gameRepository.findById(1L)).thenReturn(Optional.of(sampleGame));
        when(gameRepository.save(any(Game.class))).thenReturn(sampleGame);
        when(gameMapper.toDto(sampleGame)).thenReturn(sampleGameDto);

        gameService.update(updateDto);

        assertEquals(2, sampleGame.getCategories().size());
        assertTrue(sampleGame.getCategories().contains("Action"));
        assertTrue(sampleGame.getCategories().contains("RPG"));
    }

    // delete tests

    @Test
    void deleteById_shouldDeleteWhenExists() {
        when(gameRepository.existsById(1L)).thenReturn(true);

        gameService.deleteById(1L);

        verify(gameRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrowWhenNotFound() {
        when(gameRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> gameService.deleteById(99L));
        verify(gameRepository, never()).deleteById(any());
    }
}