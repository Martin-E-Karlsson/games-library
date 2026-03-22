package org.example.gameslibrary.dto;

import org.example.gameslibrary.entity.Game;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link Game}
 */
public class GameDto implements Serializable {
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDate releaseDate;
    private final BigDecimal price;
    private final List<String> categories;

    public GameDto(Long id, String title, String description, LocalDate releaseDate, BigDecimal price, List<String> categories) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.price = price;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<String> getCategories() {
        return categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameDto entity = (GameDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.title, entity.title) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.releaseDate, entity.releaseDate) &&
                Objects.equals(this.price, entity.price) &&
                Objects.equals(this.categories, entity.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, releaseDate, price, categories);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "title = " + title + ", " +
                "description = " + description + ", " +
                "releaseDate = " + releaseDate + ", " +
                "price = " + price + ", " +
                "categories = " + categories + ")";
    }
}