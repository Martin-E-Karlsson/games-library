package org.example.gameslibrary.repository;

import org.example.gameslibrary.entity.Game;
import org.springframework.data.repository.ListCrudRepository;

public interface GameRepository extends ListCrudRepository<Game, Long> {
}