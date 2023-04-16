package rru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    long id;

    String name;

    String description;

    LocalDate releaseDate;

    Integer duration;
    Mpa mpa;
    List<Genre> genres = new ArrayList<>();

    public Film() {
    }

    public Film(long id, String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public void createGenre(Genre genre) {
        genres.add(genre);
    }
}
