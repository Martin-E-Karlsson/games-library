package org.example.gameslibrary.controller;

import org.example.gameslibrary.dto.GameDto;
import org.example.gameslibrary.exception.ResourceNotFoundException;
import org.example.gameslibrary.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    private GameDto sampleGameDto() {
        return new GameDto(1L, "Test Game", "A test description",
                LocalDate.of(2023, 1, 15), new BigDecimal("29.99"), List.of("Action"));
    }

    // List tests

    @Test
    void listGames_shouldReturnListView() throws Exception {
        Page<GameDto> page = new PageImpl<>(List.of(sampleGameDto()), PageRequest.of(0, 20), 1);
        when(gameService.findAll(isNull(), isNull(), any())).thenReturn(page);

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(view().name("games/list"))
                .andExpect(model().attributeExists("games"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    void listGames_shouldPassFilterParameters() throws Exception {
        Page<GameDto> page = new PageImpl<>(List.of(), PageRequest.of(0, 20), 0);
        when(gameService.findAll(any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/games").param("title", "Test").param("category", "Action"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("title", "Test"))
                .andExpect(model().attribute("category", "Action"));

        verify(gameService).findAll(eq("Test"), eq("Action"), any());
    }

    // Create form tests

    @Test
    void showCreateForm_shouldReturnCreateView() throws Exception {
        mockMvc.perform(get("/games/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("games/create"))
                .andExpect(model().attributeExists("game"));
    }

    @Test
    void createGame_shouldRedirectOnSuccess() throws Exception {
        when(gameService.create(any())).thenReturn(sampleGameDto());

        mockMvc.perform(post("/games/create")
                        .param("title", "New Game")
                        .param("description", "A new game")
                        .param("releaseDate", "2023-06-01")
                        .param("price", "19.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/games"));
    }

    @Test
    void createGame_shouldReturnFormOnValidationError() throws Exception {
        mockMvc.perform(post("/games/create")
                        .param("title", "")
                        .param("description", "")
                        .param("price", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("games/create"))
                .andExpect(model().attributeHasFieldErrors("createGameDto", "title", "description"));
    }

    // Edit form tests

    @Test
    void showEditForm_shouldReturnEditView() throws Exception {
        when(gameService.findById(1L)).thenReturn(sampleGameDto());

        mockMvc.perform(get("/games/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("games/edit"))
                .andExpect(model().attributeExists("game"));
    }

    @Test
    void updateGame_shouldRedirectOnSuccess() throws Exception {
        when(gameService.update(any())).thenReturn(sampleGameDto());

        mockMvc.perform(post("/games/edit/1")
                        .param("title", "Updated Game")
                        .param("description", "Updated description")
                        .param("releaseDate", "2023-06-01")
                        .param("price", "39.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/games"));
    }

    // Delete tests

    @Test
    void deleteGame_shouldRedirectAfterDelete() throws Exception {
        doNothing().when(gameService).deleteById(1L);

        mockMvc.perform(post("/games/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/games"));

        verify(gameService).deleteById(1L);
    }

    @Test
    void deleteGame_shouldHandleNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Game not found with id: 99"))
                .when(gameService).deleteById(99L);

        mockMvc.perform(post("/games/delete/99"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
}