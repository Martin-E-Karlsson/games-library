package org.example.gameslibrary.config;

import org.example.gameslibrary.entity.Game;
import org.example.gameslibrary.repository.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Profile("dev")
@Component
public class DataSeeder implements CommandLineRunner {

    private final GameRepository gameRepository;

    public DataSeeder(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public void run(String... args) {
        if (gameRepository.count() > 0) {
            return;
        }

        List<Game> games = List.of(
                createGame("The Witcher 3", "Open world RPG set in a fantasy universe", LocalDate.of(2015, 5, 19), new BigDecimal("39.99"), List.of("RPG", "Open World")),
                createGame("Stardew Valley", "Farming simulation with RPG elements", LocalDate.of(2016, 2, 26), new BigDecimal("14.99"), List.of("Simulation", "Indie")),
                createGame("Hollow Knight", "Metroidvania action adventure", LocalDate.of(2017, 2, 24), new BigDecimal("14.99"), List.of("Platformer", "Indie")),
                createGame("Celeste", "Challenging precision platformer", LocalDate.of(2018, 1, 25), new BigDecimal("19.99"), List.of("Platformer", "Indie")),
                createGame("Hades", "Roguelike dungeon crawler", LocalDate.of(2020, 9, 17), new BigDecimal("24.99"), List.of("Roguelike", "Action")),
                createGame("Elden Ring", "Open world action RPG", LocalDate.of(2022, 2, 25), new BigDecimal("59.99"), List.of("RPG", "Action", "Open World")),
                createGame("Disco Elysium", "Detective RPG with deep narrative", LocalDate.of(2019, 10, 15), new BigDecimal("39.99"), List.of("RPG", "Adventure")),
                createGame("Factorio", "Factory building automation game", LocalDate.of(2020, 8, 14), new BigDecimal("35.00"), List.of("Strategy", "Simulation")),
                createGame("Terraria", "2D sandbox adventure", LocalDate.of(2011, 5, 16), new BigDecimal("9.99"), List.of("Sandbox", "Adventure")),
                createGame("Portal 2", "First person puzzle game", LocalDate.of(2011, 4, 19), new BigDecimal("9.99"), List.of("Puzzle", "Action")),
                createGame("Sekiro", "Intense action combat game", LocalDate.of(2019, 3, 22), new BigDecimal("59.99"), List.of("Action", "Adventure")),
                createGame("Cuphead", "Run and gun with retro cartoon style", LocalDate.of(2017, 9, 29), new BigDecimal("19.99"), List.of("Platformer", "Action")),
                createGame("Subnautica", "Underwater survival exploration", LocalDate.of(2018, 1, 23), new BigDecimal("29.99"), List.of("Survival", "Open World")),
                createGame("Dead Cells", "Roguelike metroidvania", LocalDate.of(2018, 8, 7), new BigDecimal("24.99"), List.of("Roguelike", "Action")),
                createGame("Slay the Spire", "Deck building roguelike", LocalDate.of(2019, 1, 23), new BigDecimal("24.99"), List.of("Roguelike", "Strategy")),
                createGame("Outer Wilds", "Space exploration mystery", LocalDate.of(2019, 5, 28), new BigDecimal("24.99"), List.of("Adventure", "Puzzle")),
                createGame("Undertale", "RPG with unique combat system", LocalDate.of(2015, 9, 15), new BigDecimal("9.99"), List.of("RPG", "Indie")),
                createGame("Risk of Rain 2", "Third person roguelike shooter", LocalDate.of(2020, 8, 11), new BigDecimal("24.99"), List.of("Roguelike", "Action")),
                createGame("Valheim", "Viking survival exploration", LocalDate.of(2021, 2, 2), new BigDecimal("19.99"), List.of("Survival", "Open World")),
                createGame("Inscryption", "Card game horror hybrid", LocalDate.of(2021, 10, 19), new BigDecimal("19.99"), List.of("Strategy", "Horror")),
                createGame("Baldurs Gate 3", "Epic fantasy RPG", LocalDate.of(2023, 8, 3), new BigDecimal("59.99"), List.of("RPG", "Adventure")),
                createGame("Returnal", "Roguelike third person shooter", LocalDate.of(2023, 2, 15), new BigDecimal("59.99"), List.of("Roguelike", "Action")),
                createGame("Tunic", "Isometric action adventure", LocalDate.of(2022, 3, 16), new BigDecimal("29.99"), List.of("Adventure", "Indie")),
                createGame("Noita", "Physics based roguelike", LocalDate.of(2020, 10, 15), new BigDecimal("19.99"), List.of("Roguelike", "Simulation")),
                createGame("Into the Breach", "Turn based tactical strategy", LocalDate.of(2018, 2, 27), new BigDecimal("14.99"), List.of("Strategy", "Indie"))
        );

        gameRepository.saveAll(games);
    }

    private Game createGame(String title, String description, LocalDate releaseDate, BigDecimal price, List<String> categories) {
        Game game = new Game();
        game.setTitle(title);
        game.setDescription(description);
        game.setReleaseDate(releaseDate);
        game.setPrice(price);
        game.setCategories(categories);
        return game;
    }
}