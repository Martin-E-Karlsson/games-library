package org.example.gameslibrary.repository;

import org.example.gameslibrary.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends ListCrudRepository<Game, Long>, PagingAndSortingRepository<Game, Long> {

    Page<Game> findAll(Pageable pageable);

    Page<Game> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("select distinct g from Game g join g.categories c where lower(c) = lower(:category)")
    Page<Game> findByCategory(@Param("category") String category, Pageable pageable);

    @Query("select distinct g from Game g join g.categories c where lower(g.title) like lower(concat('%', :title, '%')) and lower(c) = lower(:category)")
    Page<Game> findByTitleContainingIgnoreCaseAndCategory(@Param("title") String title, @Param("category") String category, Pageable pageable);
}