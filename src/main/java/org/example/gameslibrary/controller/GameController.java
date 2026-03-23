package org.example.gameslibrary.controller;

import jakarta.validation.Valid;
import org.example.gameslibrary.dto.CreateGameDto;
import org.example.gameslibrary.dto.GameDto;
import org.example.gameslibrary.dto.UpdateGameDto;
import org.example.gameslibrary.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    private static final String REDIRECT_PREFIX = "redirect:/games";

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public String listGames(@RequestParam(required = false) String title,
                            @RequestParam(required = false) String category,
                            @RequestParam(defaultValue = "0") int page,
                            Model model) {
        Page<GameDto> gamePage = gameService.findAll(title, category, PageRequest.of(page, 20));
        model.addAttribute("games", gamePage.getContent());
        model.addAttribute("currentPage", gamePage.getNumber());
        model.addAttribute("totalPages", gamePage.getTotalPages());
        model.addAttribute("title", title);
        model.addAttribute("category", category);
        return "games/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("game", new CreateGameDto());
        return "games/create";
    }

    @PostMapping("/create")
    public String createGame(@Valid @ModelAttribute("game") CreateGameDto game, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("game", game);
            model.addAttribute("bindingResult", bindingResult);
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
    public String updateGame(@PathVariable Long id, @Valid @ModelAttribute("game") UpdateGameDto game,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("game", game);
            model.addAttribute("bindingResult", bindingResult);
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