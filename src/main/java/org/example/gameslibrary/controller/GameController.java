package org.example.gameslibrary.controller;

import jakarta.validation.Valid;
import org.example.gameslibrary.dto.CreateGameDto;
import org.example.gameslibrary.dto.GameDto;
import org.example.gameslibrary.dto.UpdateGameDto;
import org.example.gameslibrary.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    private static final String REDIRECT_PREFIX = "redirect:/games";

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public String listGames(Model model) {
        List<GameDto> games = gameService.findAll();
        model.addAttribute("games", games);
        return "games/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("game", new CreateGameDto());
        return "games/create";
    }

    @PostMapping("/create")
    public String createGame(@Valid CreateGameDto game, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("game", game);
            return "games/create";
        }
        gameService.create(game);
        return REDIRECT_PREFIX;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        GameDto game = gameService.findById(id);
        model.addAttribute("game", game);
        return "games/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateGame(@PathVariable Long id, @Valid UpdateGameDto game,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("game", game);
            return "games/edit";
        }
        game.setId(id);
        gameService.update(game);
        return REDIRECT_PREFIX;
    }

    @PostMapping("/delete/{id}")
    public String deleteGame(@PathVariable Long id) {
        gameService.deleteById(id);
        return REDIRECT_PREFIX;
    }
}